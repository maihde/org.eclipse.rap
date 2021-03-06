/*******************************************************************************
 * Copyright (c) 2002, 2011 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.widgets;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;


public class Group_Test extends TestCase {

  private Shell shell;

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    shell = new Shell( display , SWT.NONE );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testText() {
    Group group = new Group( shell, SWT.NONE );
    assertEquals( "", group.getText() );
    group.setText( "xyz" );
    assertEquals( "xyz", group.getText() );
    try {
      group.setText( null );
      fail( "Must not allow to set null-text." );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testComputeSize() {
    Group group = new Group( shell, SWT.NONE );
    group.setLayout( new FillLayout( SWT.VERTICAL ) );
    new Button( group, SWT.RADIO ).setText( "Radio 1" );
    new Button( group, SWT.RADIO ).setText( "Radio 2" );
    Point expected = new Point( 115, 101 );
    assertEquals( expected, group.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    group.setText( "This is a very long group title." );
    expected = new Point( 204, 101 );
    assertEquals( expected, group.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    group = new Group( shell, SWT.BORDER );
    group.setLayout( new FillLayout( SWT.VERTICAL ) );
    new Button( group, SWT.RADIO ).setText( "Radio 1" );
    new Button( group, SWT.RADIO ).setText( "Radio 2" );
    expected = new Point( 119, 105 );
    assertEquals( expected, group.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    // hint + trimmings + border
    expected = new Point( 142, 159 );
    assertEquals( expected, group.computeSize( 100, 100 ) );
  }

  public void testComputeTrim() {
    Group group = new Group( shell, SWT.NONE );
    // trimmings = 3, 17, 6, 20
    Rectangle expected = new Rectangle( -19, -36, 38, 55 );
    assertEquals( expected, group.computeTrim( 0, 0, 0, 0 ) );

    expected = new Rectangle( 1, -16, 138, 155 );
    assertEquals( expected, group.computeTrim( 20, 20, 100, 100 ) );
  }

  public void testClientArea() {
    Group group = new Group( shell, SWT.NONE );
    group.setText( "This is a very long group title." );
    group.setSize( 100, 100 );
    group.setLayout( new FillLayout( SWT.VERTICAL ) );
    new Button( group, SWT.RADIO ).setText( "Radio 1" );
    new Button( group, SWT.RADIO ).setText( "Radio 2" );

    // trimmings = 3, 17, 6, 20
    Rectangle expected = new Rectangle( 19, 36, 62, 45 );
    assertEquals( expected, group.getClientArea() );
  }
  
  public void testIsSerializable() throws Exception {
    String groupText = "text";
    Group group = new Group( shell, SWT.NONE );
    group.setText( groupText );
    
    Group deserializedGroup = Fixture.serializeAndDeserialize( group );
    
    assertEquals( groupText, deserializedGroup.getText() );
  }
}
