/*
 * Created on 2005-10-04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.antiaction.mayhem.httpd.filters;

import java.util.zip.GZIPOutputStream;

/**
 * @author Nicholas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GZipFilter {

	/** GZIP OutputStream. */
	private GZIPOutputStream gzipOut;

//	dres.setHeader( "Content-Encoding", "gzip" );
	/*
				if ( bytes != null ) {
					contentLength = bytes.length;
					//byte[] bytesGZiped = null;
					try {
						ByteArrayOutputStream gzipOutStream = new ByteArrayOutputStream( bytes.length );
						gzipOutStream.reset();
						GZIPOutputStream gzipStream = new GZIPOutputStream( gzipOutStream, bytes.length );
						gzipStream.write( bytes, 0, bytes.length );
						gzipStream.finish();
						bytesGZiped = gzipOutStream.toByteArray();
					}
					catch (IOException e) {
					}
				}
	*/

}
