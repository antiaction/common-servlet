/*
 * Experimental response gzip filter.
 * Copyright (C) 2004  Nicholas Clarke
 *
 */

/*
 * History:
 *
 * 04-Oct-2004 : Initial implementation.
 *
 */

package com.antiaction.mayhem.httpd.filters;

import java.util.zip.GZIPOutputStream;

/**
 * Experimental response gzip filter.
 *
 * @version 1.00
 * @author Nicholas Clarke <mayhem[at]antiaction[dot]com>
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
