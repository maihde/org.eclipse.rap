/*******************************************************************************
 * Copyright (c) 2002, 2012 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.widgets;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CallOperation;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rap.rwt.testfixture.Message.Operation;
import org.eclipse.rwt.internal.application.RWTFactory;
import org.eclipse.rwt.internal.lifecycle.EntryPointUtil;
import org.eclipse.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.rwt.internal.service.RequestParams;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;


public class ExternalBrowser_Test extends TestCase {

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testOpen() {
    new Display();
    // Test illegal arguments
    try {
      ExternalBrowser.open( null, "http://nowhere.org", 0 );
      fail( "ExternalBrowser#open must not allow id == null" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      ExternalBrowser.open( "", "http://nowhere.org", 0 );
      fail( "ExternalBrowser#open must not allow id == empty string" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      ExternalBrowser.open( "myId", null, 0 );
      fail( "ExternalBrowser#open must not allow url == null" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testClose() {
    new Display();
    // Test illegal arguments
    try {
      ExternalBrowser.close( null );
      fail( "ExternalBrowser#close must not allow id == null" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      ExternalBrowser.close( "" );
      fail( "ExternalBrowser#close must not allow id == empty string" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  /* (intentionally non-JavaDoc'ed)
   * Ensure that the order in which the protocol messages are rendered
   * matches the order of the ExternalBrowser#open/close calls
   */
  public void testExecutionOrder() throws IOException {
    RWTFactory.getEntryPointManager().registerByName( EntryPointUtil.DEFAULT,
                                                      TestExecutionOrderEntryPoint.class );
    Fixture.fakeNewRequest();
    Fixture.fakeRequestParam( RequestParams.UIROOT, "w1" );

    RWTLifeCycle lifeCycle = ( RWTLifeCycle )RWTFactory.getLifeCycleFactory().getLifeCycle();
    lifeCycle.execute();

    Message message = Fixture.getProtocolMessage();
    int createIndex = indexOfCreateOperation( message );
    int open1Index = indexOfCallOperation( message, "open", "1" );
    int close1Index = indexOfCallOperation( message, "close", "1" );
    int open2Index = indexOfCallOperation( message, "open", "2" );
    int close2Index = indexOfCallOperation( message, "close", "2" );
    assertTrue( createIndex != -1 && createIndex < open1Index );
    assertTrue( open1Index != -1 && close1Index != -1 );
    assertTrue( open2Index != -1 && close2Index != -1 );
    assertTrue( open1Index < close1Index );
    assertTrue( open2Index < close2Index );
    assertTrue( open1Index < open2Index );
  }

  private int indexOfCreateOperation( Message message ) {
    int result = -1;
    int operationCount = message.getOperationCount();
    for( int position = 0; position < operationCount; position++ ) {
      Operation operation = message.getOperation( position );
      if( operation instanceof CreateOperation ) {
        CreateOperation createOperation = ( CreateOperation )operation;
        if( createOperation.getTarget().equals( "eb" ) ) {
          result = position;
        }
      }
    }
    return result;
  }

  private int indexOfCallOperation( Message message, String method, String idProperty ) {
    int result = -1;
    int operationCount = message.getOperationCount();
    for( int position = 0; position < operationCount; position++ ) {
      Operation operation = message.getOperation( position );
      if( operation instanceof CallOperation ) {
        CallOperation callOperation = ( CallOperation )operation;
        if(    method.equals( callOperation.getMethodName() )
            && idProperty.equals( callOperation.getProperty( "id" ) ) )
        {
          result = position;
        }
      }
    }
    return result;
  }

  public static final class TestExecutionOrderEntryPoint implements IEntryPoint {

    public int createUI() {
      new Display();
      // execute a row open/close method calls
      ExternalBrowser.open( "1",
                            "http://eclipse.org",
                            ExternalBrowser.STATUS | ExternalBrowser.LOCATION_BAR );
      ExternalBrowser.close( "1" );
      ExternalBrowser.open( "2",
                            "http://eclipse.org",
                            ExternalBrowser.STATUS | ExternalBrowser.LOCATION_BAR );
      ExternalBrowser.close( "2" );
      return 0;
    }
  }
}
