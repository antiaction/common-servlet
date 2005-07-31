/*
 * Asynchronious logging filter.
 * Copyright (C) 2004, 2005  Nicholas Clarke
 *
 */

/*
 * History:
 *
 * 04-Mar-2005 : Refactoring, made storing asynchronious.
 *
 */

package com.antiaction.mayhem.httpd.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
//import javax.sql.ConnectionPoolDataSource;

import java.util.List;
import java.util.ArrayList;

/**
 * Asynchronious logging filter.
 *
 * @version 1.00
 * @author Nicholas Clarke <mayhem[at]antiaction[dot]com>
 */
public class LogFilter implements Filter, Runnable {

	/** Shutdown boolean. */
	private boolean exit = false;

	//private FilterConfig config;
	private String dsName;
	private Context ctx;
	private DataSource ds;

	private Object syncObj = new Object();

	private List entries = new ArrayList();

	public void init(FilterConfig filterConfig) throws ServletException {
		//config = filterConfig;
		dsName = filterConfig.getInitParameter( "datasource" );
		if ( dsName != null && !dsName.equals( "" ) ) {
			try {
				ctx = new InitialContext();
				ds = (DataSource)ctx.lookup( dsName );
			}
			catch (NamingException e) {
				System.out.println( e );
			}
		}
		Thread t = new Thread( this );
		t.start();
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		//HttpServletResponse resp = (HttpServletResponse)response;

		HttpSession httpSession = req.getSession( true );

		long enterTime = System.currentTimeMillis();

		chain.doFilter( request, response );

		long exitTime = System.currentTimeMillis();

		String resource = req.getRequestURI();
		String queryString = req.getQueryString();
		if ( queryString != null ) {
			resource += "?" + queryString;
		}

		String sessionId = null;
		if ( httpSession != null ) {
			sessionId = httpSession.getId();
		}

		FilterEntry entry = new FilterEntry();
		entry.timestamp = new Timestamp( enterTime );
		entry.clientIp = request.getRemoteAddr();
		entry.referer = req.getHeader( "referer" );
		entry.method = req.getMethod();
		entry.resourcePath = resource;
		entry.sessionId = sessionId;
		entry.statusCode = 1;
		entry.processTime = (int)(exitTime - enterTime);

		synchronized ( syncObj ) {
			entries.add( entry );
		}
	}

	class FilterEntry {
		Timestamp timestamp;
		String clientIp;
		String referer;
		String method;
		String resourcePath;
		String sessionId;
		int statusCode;
		int processTime;
	}

	public void run() {
		while ( !exit ) {
			try {
				Thread.sleep( 60 * 1000 );

				List tmpEntries = null;
				synchronized ( syncObj ) {
					if ( entries != null && entries.size() > 0 ) {
						tmpEntries = entries;
						entries = new ArrayList();
					}
				}

				if ( tmpEntries != null && tmpEntries.size() > 0 ) {
					Connection conn = null;
					String insertSql;
					ResultSet rs = null;
					PreparedStatement insertStm;

					try {
						if ( ds != null ) {
							conn = ds.getConnection();
							if ( conn != null ) {
								conn.setCatalog( "alfachins" );
								conn.setAutoCommit( false );

								insertSql = "";
								insertSql += "INSERT INTO accesslog";
								insertSql += "(timestamp, clientIp, referer, method, resourcePath, sessionId, statusCode, processTime) " + "\n";
								insertSql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?)" + "\n";
								insertStm = conn.prepareStatement( insertSql, Statement.RETURN_GENERATED_KEYS  );

								for ( int i=0; i<tmpEntries.size(); ++i ) {
									FilterEntry entry = (FilterEntry)tmpEntries.get( i );

									insertStm.setTimestamp( 1, entry.timestamp );
									insertStm.setString( 2, entry.clientIp );
									insertStm.setString( 3, entry.referer );
									insertStm.setString( 4, entry.method );
									insertStm.setString( 5, entry.resourcePath );
									insertStm.setString( 6, entry.sessionId );
									insertStm.setInt( 7, entry.statusCode );
									insertStm.setInt( 8, entry.processTime );
									insertStm.executeUpdate();

									rs = insertStm.getGeneratedKeys();
									if ( rs != null & rs.next() ) {
										//System.out.println( rs.getLong( 1 ) );
									}
									insertStm.clearParameters();

									conn.commit();
								}
								conn.close();
							}
						}
					}
					catch (SQLException e) {
						System.out.println( e );
					}
				}
			}
			catch (InterruptedException e) {
			}
		}
	}

}
