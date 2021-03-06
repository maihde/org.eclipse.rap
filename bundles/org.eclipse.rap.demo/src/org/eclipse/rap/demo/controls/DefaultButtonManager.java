/*******************************************************************************
 * Copyright (c) 2002, 2011 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rap.demo.controls;

import java.util.*;

import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;


final class DefaultButtonManager extends SessionSingletonBase {

  static final class ChangeEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    public ChangeEvent( final Shell source ) {
      super( source );
    }
  }

  static interface ChangeListener extends EventListener {
    void defaultButtonChanged( ChangeEvent event );
  }

  static DefaultButtonManager getInstance() {
    return getInstance( DefaultButtonManager.class );
  }

  private final Set<ChangeListener> changeListeners = new HashSet<ChangeListener>();

  private DefaultButtonManager() {
    // prevent instantiation from outside
  }

  void change( final Shell shell, final Button defaultButton ) {
    shell.setDefaultButton( defaultButton );
    if( changeListeners.size() > 0 ) {
      ChangeListener[] listeners = new ChangeListener[ changeListeners.size() ];
      changeListeners.toArray( listeners );
      ChangeEvent event = new ChangeEvent( shell );
      for( int i = 0; i < listeners.length; i++ ) {
        listeners[ i ].defaultButtonChanged( event );
      }
    }
  }

  void addChangeListener( final ChangeListener listener ) {
    changeListeners.add( listener );
  }

  void removeChangeListener( final ChangeListener listener ) {
    changeListeners.remove( listener );
  }
}
