/*
 * Created on 16-03-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.antiaction.mayhem.httpd.filters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nicholas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ImageFilter implements Filter {

	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		ServletContext servletContext = filterConfig.getServletContext();

		String pxStr = request.getParameter( "px" );
		if ( pxStr != null && pxStr.length() != 0 ) {
			int px = 0;
			int idx;
			try {
				px = Integer.parseInt( pxStr );

				String path = req.getPathInfo();
				String realPath = servletContext.getRealPath( path );

				int slashIdx = path.lastIndexOf( "/" );
				String fileStr = null;
				String extStr = null;
				if ( slashIdx != -1 ) {
					fileStr = path.substring( slashIdx + 1, path.length() );
				}
				if ( fileStr != null ) {
					extStr = servletContext.getMimeType( fileStr );
				}

				// debug
				//System.out.println( path );
				//System.out.println( realPath );

				//String outfile = path.substring( idx + 1, path.length() );
				//System.out.println( outfile + ".1" );

				String outfile = realPath + "-" + px;

				File outFile = new File( outfile );
				if ( !outFile.exists() ) {
					scaleImage( realPath, px, px, outfile, 75 );
				}

				byte[] bytes = null;
				bytes = new byte[ 65536 ];

				if ( extStr != null ) {
					resp.setContentType( extStr );
				}

				ServletOutputStream out = resp.getOutputStream();
				InputStream is = new BufferedInputStream( new FileInputStream( outfile ) );
				int len;
				while ( ( len = is.read( bytes, 0, 65536 ) ) != -1 ) {
					out.write( bytes, 0, len );
				}
				is.close();

				out.flush();
				out.close();
				return;
			}
			catch (NumberFormatException e) {
			}
			System.out.println( pxStr );
		}

		chain.doFilter( request, response );
	}

	public synchronized void scaleImage(String infile, int destWidth, int destHeight, String outfile, int quality) {
		// load image from INFILE
		Image image = Toolkit.getDefaultToolkit().getImage( infile );
		MediaTracker mediaTracker = new MediaTracker( new Container() );
		mediaTracker.addImage( image, 0 );
		try {
			mediaTracker.waitForID( 0 );
		}
		catch (InterruptedException e) {
		}

		// determine thumbnail size from WIDTH and HEIGHT
		double thumbRatio = (double)destWidth / (double)destHeight;
		int imageWidth = image.getWidth( null );
		int imageHeight = image.getHeight( null );
		double imageRatio = (double)imageWidth / (double)imageHeight;
		if ( thumbRatio < imageRatio ) {
			destHeight = (int)( destWidth / imageRatio );
		} else {
			destWidth = (int)( destHeight * imageRatio );
		}

		// draw original image to thumbnail image object and
		// scale it to the new size on-the-fly
		BufferedImage destImage = new BufferedImage( destWidth, destHeight, BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics2D = destImage.createGraphics();
		//graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );
		graphics2D.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		graphics2D.drawImage( image, 0, 0, destWidth, destHeight, null );

		// save thumbnail image to OUTFILE
		try {
			BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( outfile ) );
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam( destImage );
			quality = Math.max( 0, Math.min( quality, 100 ) );
			param.setQuality( (float)quality / 100.0f, false );
			encoder.setJPEGEncodeParam( param );
			encoder.encode( destImage );
			out.close(); 
		}
		catch (FileNotFoundException e) {
			System.out.println( e );
		}
		catch (IOException e) {
			System.out.println( e );
		}

		graphics2D.dispose();
		graphics2D = null;

		image.flush();
		image = null;

		destImage.flush();
		destImage = null;
	}

}
