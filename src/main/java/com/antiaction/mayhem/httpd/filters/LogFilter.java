package com.antiaction.mayhem.httpd.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
//import javax.sql.ConnectionPoolDataSource;

public class LogFilter implements Filter {

	private FilterConfig config;
	private String dsName;
	private Context ctx;
	private DataSource ds;

	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
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
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;

		HttpSession httpSession = req.getSession( true );

		long enterTime = System.currentTimeMillis();

		chain.doFilter( request, response );

		long exitTime = System.currentTimeMillis();

		Connection conn = null;
		String insertSql;
		Statement stm = null;
		ResultSet rs = null;
		PreparedStatement insertStm;
		try {
			if ( ds != null ) {
				conn = ds.getConnection();
				if ( conn != null ) {
					conn.setCatalog( "alfachins" );

					insertSql = "";
					insertSql += "INSERT INTO accesslog";
					insertSql += "(timestamp, clientIp, referer, method, resourcePath, sessionId, statusCode, processTime) " + "\n";
					insertSql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?)" + "\n";
					insertStm = conn.prepareStatement( insertSql, Statement.RETURN_GENERATED_KEYS  );

					String resource = req.getRequestURI();
					String queryString = req.getQueryString();
					if ( queryString != null ) {
						resource += "?" + queryString;
					}

					String sessionId = null;
					if ( httpSession != null ) {
						sessionId = httpSession.getId();
					}

					insertStm.setTimestamp( 1, new java.sql.Timestamp( System.currentTimeMillis() ) );
					insertStm.setString( 2, request.getRemoteAddr() );
					insertStm.setString( 3, req.getHeader( "referer" ) );
					insertStm.setString( 4, req.getMethod() );
					insertStm.setString( 5, resource );
					insertStm.setString( 6, sessionId );
					insertStm.setInt( 7, 1 );
					insertStm.setInt( 8, (int)(exitTime - enterTime) );
					insertStm.executeUpdate();

					rs = insertStm.getGeneratedKeys();
					if ( rs != null & rs.next() ) {
						//System.out.println( rs.getLong( 1 ) );
					}
					insertStm.clearParameters();

					conn.commit();
					conn.close();
				}
			}
		}
		catch (SQLException e) {
			System.out.println( e );
		}
	}

}
