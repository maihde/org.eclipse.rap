/*******************************************************************************
 * Copyright (c) 2011 Frank Appel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Frank Appel - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.internal.util;

import java.io.*;


public class StreamUtil {

  public static void write( byte[] content, OutputStream out ) throws IOException {
    out.write( content );
    out.flush();
  }

  public static void writeBuffered( byte[] content, OutputStream out ) throws IOException {
    write( content, new BufferedOutputStream( out ) );
  }

  public static void close( InputStream inputStream ) {
    try {
      inputStream.close();
    } catch( IOException ioe ) {
      throw new RuntimeException( "Failed to close input stream.", ioe );
    }
  }
}
