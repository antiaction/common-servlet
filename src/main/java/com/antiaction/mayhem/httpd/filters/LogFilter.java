package com.antiaction.mayhem.httpd.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;

public class LogFilter implements Filter {

	FilterConfig config;

	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// debug
		System.out.println( "Filter" );

		chain.doFilter( request, response );
	}

}
