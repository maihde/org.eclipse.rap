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
package org.eclipse.swt.events;

import org.eclipse.rwt.Adaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.widgets.EventUtil;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;


/**
 * Instances of this class are sent as a result of
 * menus being shown and hidden.
 *
 * <p><strong>IMPORTANT:</strong> All <code>public static</code> members of
 * this class are <em>not</em> part of the RWT public API. They are marked
 * public only so that they can be shared within the packages provided by RWT.
 * They should never be accessed from application code.
 * </p>
 *
 * @see MenuListener
 * @since 1.0
 */
public final class MenuEvent extends TypedEvent {

  private static final long serialVersionUID = 1L;

  public static final int MENU_SHOWN = SWT.Show;
  public static final int MENU_HIDDEN = SWT.Hide;
  private static final Class LISTENER = MenuListener.class;

  /**
   * Constructs a new instance of this class based on the
   * information in the given untyped event.
   *
   * @param event the untyped event containing the information
   */
  public MenuEvent( Event event ) {
    super( event );
  }

  /**
   * Constructs a new instance of this class.
   * <p><strong>IMPORTANT:</strong> This method is <em>not</em> part of the RWT
   * public API. It is marked public only so that it can be shared
   * within the packages provided by RWT. It should never be accessed
   * from application code.
   * </p>
   */
  public MenuEvent( Widget widget, int id ) {
    super( widget, id );
  }

  protected void dispatchToObserver( Object listener ) {
    switch( getID() ) {
      case MENU_SHOWN:
        ( ( MenuListener )listener ).menuShown( this );
      break;
      case MENU_HIDDEN:
        ( ( MenuListener )listener ).menuHidden( this );
      break;
      default:
        throw new IllegalStateException( "Invalid event handler type." );
    }
  }

  protected Class getListenerType() {
    return LISTENER;
  }

  protected boolean allowProcessing() {
    return EventUtil.isAccessible( widget );
  }

  public static boolean hasListener( Adaptable adaptable ) {
    return hasListener( adaptable, LISTENER );
  }

  public static void addListener( Adaptable adaptable, MenuListener listener ) {
    addListener( adaptable, LISTENER, listener );
  }

  public static void removeListener( Adaptable adaptable, MenuListener listener ) {
    removeListener( adaptable, LISTENER, listener );
  }

  public static Object[] getListeners( Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}