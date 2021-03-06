/*******************************************************************************
 * Copyright (c) 2007, 2012 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.widgets.ITableAdapter;


public class TableColumn_Test extends TestCase {

  private Display display;
  private Shell shell;

  public void testCreation() {
    Table table = new Table( shell, SWT.NONE );
    // Add one item
    TableColumn col1 = new TableColumn( table, SWT.NONE );
    assertEquals( 1, table.getColumnCount() );
    assertSame( col1, table.getColumn( 0 ) );
    // Insert an item before first item
    TableColumn col0 = new TableColumn( table, SWT.NONE, 0 );
    assertEquals( 2, table.getColumnCount() );
    assertSame( col0, table.getColumn( 0 ) );
    // Try to add an item whit an index which is out of bounds
    try {
      new TableColumn ( table, SWT.NONE, table.getColumnCount() + 8 );
      fail();
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testParent() {
    Table table = new Table( shell, SWT.NONE );
    // Test creating column with valid parent
    TableColumn column = new TableColumn( table, SWT.NONE );
    assertSame( table, column.getParent() );
    // Test creating column without parent
    try {
      new TableColumn( null, SWT.NONE );
      fail( "Must not allow to create TableColumn withh null-parent." );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
  }

  public void testDisplay() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    assertSame( display, column.getDisplay() );
  }

  public void testStyle() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    assertTrue( ( column.getStyle() & SWT.LEFT ) != 0 );
    column = new TableColumn( table, SWT.LEFT | SWT.RIGHT | SWT.CENTER );
    assertTrue( ( column.getStyle() & SWT.LEFT ) != 0 );
    column = new TableColumn( table, SWT.RIGHT );
    assertTrue( ( column.getStyle() & SWT.RIGHT ) != 0 );
  }

  public void testInitialValues() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    assertEquals( 0, column.getWidth() );
    assertEquals( "", column.getText() );
    assertEquals( null, column.getToolTipText() );
    assertNull( column.getImage() );
    assertEquals( true, column.getResizable() );
    assertEquals( false, column.getMoveable() );
    assertEquals( SWT.LEFT, column.getAlignment() );
  }

  public void testAlignment() {
    Table table = new Table( shell, SWT.NONE );

    TableColumn column;
    column = new TableColumn( table, SWT.NONE );
    assertEquals( SWT.LEFT, column.getAlignment() );
    column = new TableColumn( table, SWT.LEFT );
    assertEquals( SWT.LEFT, column.getAlignment() );
    column = new TableColumn( table, SWT.CENTER );
    assertEquals( SWT.CENTER, column.getAlignment() );
    column = new TableColumn( table, SWT.RIGHT );
    assertEquals( SWT.RIGHT, column.getAlignment() );

    column.setAlignment( SWT.LEFT );
    assertEquals( SWT.LEFT, column.getAlignment() );
    column.setAlignment( 4712 );
    assertEquals( SWT.LEFT, column.getAlignment() );
  }
  
  public void testInitialWidth() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    assertEquals( 0, column.getWidth() );
  }

  public void testWidth() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    column.setWidth( 70 );
    assertEquals( 70, column.getWidth() );
    column.setWidth( 0 );
    assertEquals( 0, column.getWidth() );
  }
  
  public void testWidthWithNegativeValue() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    column.setWidth( 4711 );
    column.setWidth( -1 );
    assertEquals( 4711, column.getWidth() );
    column.setWidth( -2 );
    assertEquals( 4711, column.getWidth() );
    column.setWidth( -3 );
    assertEquals( 4711, column.getWidth() );
  }

  public void testPack() {
    final java.util.List<Widget> log = new ArrayList<Widget>();
    ControlAdapter resizeListener = new ControlAdapter() {
      public void controlResized( ControlEvent e ) {
        log.add( e.widget );
      }
    };
    Table table = new Table( shell, SWT.NONE );
    table.setHeaderVisible( true );
    TableColumn column = new TableColumn( table, SWT.NONE );
    column.addControlListener( resizeListener );

    // Ensure that controlResized is fired when pack changes the width
    column.setWidth( 12312 );
    log.clear();
    column.pack();
    assertSame( log.get( 0 ), column );

    // Ensure that controlResized is *not* fired when pack doesn't change the
    // width
    log.clear();
    column.pack();
    assertEquals( 0, log.size() );

    column.removeControlListener( resizeListener );

    // pack calculates a minimal width for an empty column
    column = new TableColumn( table, SWT.NONE );
    column.pack();
    assertTrue( column.getWidth() > 0 );

    // Test that an image on a column is taken into account
    column = new TableColumn( table, SWT.NONE );
    Image image = Graphics.getImage( "resources/images/test-50x100.png",
                                     TableColumn_Test.class.getClassLoader() );
    column.setImage( image );
    column.pack();
    assertTrue( column.getWidth() >= image.getBounds().width );

    // An item wider than the column itself strechtes the column
    while( table.getColumnCount() > 0 ) {
      table.getColumn( 0 ).dispose();
    }
    table.removeAll();
    column = new TableColumn( table, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setImage( image );
    column.pack();
    assertTrue( column.getWidth() >= item.getBounds().width );
  }

  public void testGetPreferredWidthMultiLineHeader() {
    Table result = new Table( shell, SWT.NONE );
    for( int i = 0; i < 3; i++ ) {
      TableColumn column1 = new TableColumn( result, SWT.NONE );
      column1.setWidth( 50 );
      column1.setText( "Column " + i );
    }
    Table table = result;
    table.setHeaderVisible( true );
    TableColumn column = table.getColumn( 1 );

    column.setText( "Multi\nLineText" );

    assertEquals( 60, column.getPreferredWidth() );
  }

  public void testPackWithVirtual() {
    final java.util.List<Widget> log = new ArrayList<Widget>();
    Listener setDataListener = new Listener() {
      public void handleEvent( Event event ) {
        log.add( event.item );
      }
    };
    ControlListener resizeListener = new ControlAdapter() {
      public void controlResized( ControlEvent event ) {
        log.add( event.widget );
      }
    };
    Table table;
    TableColumn column;

    // Must not try to access items if there aren't any
    log.clear();
    table = new Table( shell, SWT.VIRTUAL );
    column = new TableColumn( table, SWT.NONE );
    column.setWidth( 200 );
    column.addControlListener( resizeListener );
    column.pack();
    assertEquals( 1, log.size() ); // ensure that pack() did something

    // Ensure that pack resolves only first virtual item
    log.clear();
    table = new Table( shell, SWT.VIRTUAL );
    column = new TableColumn( table, SWT.NONE );
    table.setSize( 100, 50 );
    table.setItemCount( 100 );
    table.addListener( SWT.SetData, setDataListener );
    column.pack();
    assertEquals( 1, log.size() );
  }

  public void testResizeEvent() {
    final java.util.List<ControlEvent> log = new ArrayList<ControlEvent>();
    Table table = new Table( shell, SWT.NONE );
    final TableColumn column = new TableColumn( table, SWT.NONE );
    column.addControlListener( new ControlListener() {
      public void controlMoved( ControlEvent event ) {
        fail( "unexpected event: controlMoved" );
      }
      public void controlResized( ControlEvent event ) {
        log.add( event );
      }
    } );
    ControlEvent event;
    // Changing column width leads to resize event
    log.clear();
    column.setWidth( column.getWidth() + 1 );
    assertEquals( 1, log.size() );
    event = log.get( 0 );
    assertSame( column, event.getSource() );
    // Setting the column width to the same value it already has as well leads
    // to resize event
    log.clear();
    column.setWidth( column.getWidth() );
    assertEquals( 1, log.size() );
    event = log.get( 0 );
    assertEquals( column, event.getSource() );
  }

  public void testMoveEvent() {
    final java.util.List<ControlEvent> log = new ArrayList<ControlEvent>();
    Table table = new Table( shell, SWT.NONE );
    final TableColumn column = new TableColumn( table, SWT.NONE );
    column.addControlListener( new ControlListener() {
      public void controlMoved( ControlEvent event ) {
        fail( "unexpected event: controlMoved" );
      }
      public void controlResized( ControlEvent event ) {
        log.add( event );
      }
    } );
    final TableColumn column1 = new TableColumn( table, SWT.NONE );
    column1.addControlListener( new ControlListener() {
      public void controlMoved( ControlEvent event ) {
        log.add( event );
      }
      public void controlResized( ControlEvent event ) {
        fail( "unexpected event: controlResized" );
      }
    } );
    ControlEvent event;
    // Changing column width leads to resize event and move event of the next
    // columns
    log.clear();
    column.setWidth( column.getWidth() + 1 );
    assertEquals( 2, log.size() );
    event = log.get( 0 );
    assertSame( column, event.getSource() );
    event = log.get( 1 );
    assertSame( column1, event.getSource() );
  }

  public void testDisposeLast() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    TableColumn column1 = new TableColumn( table, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setText( "itemText for column 0" );
    item.setText( 1, "itemText for column 1" );

    column1.dispose();
    assertEquals( "", item.getText( 1 ) );
    assertEquals( "itemText for column 0", item.getText() );

    column0.dispose();
    assertEquals( 0, table.getColumnCount() );
    assertEquals( 1, table.getItemCount() );
    assertEquals( "itemText for column 0", item.getText() );
  }

  // 323179: Creating and disposing a TableColumn (without updating the
  // TableItems) results in an ArrayIndexOutOfBoundsException
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=323179
  public void testCreateDisposeColumnWithoutDataUpdate() {
    Table table = new Table( shell, SWT.NONE );
    TableColumn column1 = new TableColumn( table, SWT.NONE );
    column1.setText( "First Column" );
    int number = 5;
    TableItem[] items = new TableItem[ number ];
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, SWT.NONE );
      items[ i ].setText( 0, "x1" );
    }
    TableColumn column2 = new TableColumn( table, SWT.NONE );
    column2.setText( "Second Column" );
    column2.dispose();
  }

  public void testIsFixedColumn() {
    shell.setSize( 800, 600 );
    Table table = createFixedColumnsTable( shell );
    table.setSize( 300, 100 );
    ITableAdapter adapter = table.getAdapter( ITableAdapter.class );
    assertTrue( adapter.isFixedColumn( table.getColumn( 0 ) ) );
    assertFalse( adapter.isFixedColumn( table.getColumn( 1 ) ) );
    table.setColumnOrder( new int[]{ 1, 0, 2, 3, 4, 5, 6, 7, 8, 9 } );
    assertFalse( adapter.isFixedColumn( table.getColumn( 0 ) ) );
    assertTrue( adapter.isFixedColumn( table.getColumn( 1 ) ) );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    display = new Display();
    shell = new Shell( display );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  private Table createFixedColumnsTable( Shell shell ) {
    Table result = new Table( shell, SWT.NONE );
    result.setData( RWT.FIXED_COLUMNS, new Integer( 1 ) );
    for( int i = 0; i < 10; i++ ) {
      TableColumn column = new TableColumn( result, SWT.NONE );
      column.setWidth( 50 );
    }
    return result;
  }
}
