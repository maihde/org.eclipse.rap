/*******************************************************************************
 * Copyright (c) 2008, 2011 Innoopract Informationssysteme GmbH and others.
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;


/**
 * Instances of this class are sent as a result of keys being pressed and
 * released on the keyboard.
 * <p>
 * When a key listener is added to a control, the control will take part in
 * widget traversal. By default, all traversal keys (such as the tab key and so
 * on) are delivered to the control. In order for a control to take part in
 * traversal, it should listen for traversal events. Otherwise, the user can
 * traverse into a control but not out. Note that native controls such as table
 * and tree implement key traversal in the operating system. It is not necessary
 * to add traversal listeners for these controls, unless you want to override
 * the default traversal.
 * </p>
 *
 * <p><strong>IMPORTANT:</strong> All <code>public static</code> members of
 * this class are <em>not</em> part of the RWT public API. They are marked
 * public only so that they can be shared within the packages provided by RWT.
 * They should never be accessed from application code.
 * </p>
 *
 * @see KeyListener
 * @see TraverseListener
 *
 * @since 1.2
 */
public class KeyEvent extends TypedEvent {

  private static final long serialVersionUID = 1L;

  public static final int KEY_PRESSED = SWT.KeyDown;
  public static final int KEY_RELEASED = SWT.KeyUp;

  private static final Class LISTENER = KeyListener.class;


  /**
   * the character represented by the key that was typed. This is the final
   * character that results after all modifiers have been applied. For example,
   * when the user types Ctrl+A, the character value is 0x01. It is important
   * that applications do not attempt to modify the character value based on a
   * stateMask (such as SWT.CTRL) or the resulting character will not be
   * correct.
   */
  public char character;

  /**
   * the key code of the key that was typed, as defined by the key code
   * constants in class <code>SWT</code>. When the character field of the event
   * is ambiguous, this field contains the unicode value of the original
   * character. For example, typing Ctrl+M or Return both result in the
   * character '\r' but the keyCode field will also contain '\r' when Return was
   * typed.
   *
   * @see org.eclipse.swt.SWT
   */
  public int keyCode;

  /**
   * the state of the keyboard modifier keys at the time the event was
   * generated, as defined by the key code constants in class <code>SWT</code>.
   *
   * @see org.eclipse.swt.SWT
   */
  public int stateMask;

  /**
   * A flag indicating whether the operation should be allowed. Setting this
   * field to <code>false</code> will cancel the operation.
   */
  public boolean doit;

  /**
   * Constructs a new instance of this class based on the information in the
   * given untyped event.
   *
   * @param event the untyped event containing the information
   */
  public KeyEvent( Event event ) {
    super( event );
    this.character = event.character;
    this.keyCode = event.keyCode;
    this.stateMask = event.stateMask;
    this.doit = event.doit;
  }

  public KeyEvent( Control source, int id ) {
    super( source, id );
    doit = true;
  }

  protected void dispatchToObserver( Object listener ) {
    switch( getID() ) {
      case KEY_PRESSED:
        ( ( KeyListener )listener ).keyPressed( this );
      break;
      case KEY_RELEASED:
        ( ( KeyListener )listener ).keyReleased( this );
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

  /**
   * Returns a string containing a concise, human-readable description of the
   * receiver.
   *
   * @return a string representation of the event
   */
  public String toString() {
    String string = super.toString();
    return   string.substring( 0, string.length() - 1 ) // remove trailing '}'
           + " character='"
           + ( ( character == 0 ) ? "\\0" : "" + character )
           + "'"
           + " keyCode="
           + keyCode
           + " stateMask="
           + stateMask
           + " doit="
           + doit
           + "}";
  }

  public static void addListener( Adaptable adaptable, KeyListener listener ) {
    addListener( adaptable, LISTENER, listener );
  }

  public static void removeListener( Adaptable adaptable, KeyListener listener ) {
    removeListener( adaptable, LISTENER, listener );
  }

  public static boolean hasListener( Adaptable adaptable ) {
    return hasListener( adaptable, LISTENER );
  }
}
