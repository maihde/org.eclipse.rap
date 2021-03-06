/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rwt.internal.service;

import java.io.IOException;
import java.io.NotSerializableException;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.TestSession;
import org.eclipse.rwt.internal.service.SessionStoreImpl;
import org.eclipse.rwt.service.SessionStoreEvent;
import org.eclipse.rwt.service.SessionStoreListener;


public class SessionStoreImplSerialization_Test extends TestCase {

  private static class LoggingSessionStoreListener implements SessionStoreListener {
    private static final long serialVersionUID = 1L;
    static boolean wasCalled;
    public void beforeDestroy( SessionStoreEvent event ) {
      wasCalled = true;
    }
  }

  private HttpSession httpSession;
  private SessionStoreImpl sessionStore;

  public void testAttributesAreSerializable() throws Exception {
    String attributeName = "foo";
    String attributeValue = "bar";
    sessionStore.setAttribute( attributeName, attributeValue );
    SessionStoreImpl deserializedSession = Fixture.serializeAndDeserialize( sessionStore );

    assertEquals( attributeValue, deserializedSession.getAttribute( attributeName ) );
  }

  public void testHttpSessionIsNotSerializable() throws Exception {
    SessionStoreImpl deserializedSession = Fixture.serializeAndDeserialize( sessionStore );

    assertNull( deserializedSession.getHttpSession() );
  }
  
  public void testIdIsSerializable() throws Exception {
    SessionStoreImpl deserializedSession = Fixture.serializeAndDeserialize( sessionStore );

    assertEquals( sessionStore.getId(), deserializedSession.getId() );
  }

  public void testBoundIsSerializable() throws Exception {
    SessionStoreImpl deserializedSession = Fixture.serializeAndDeserialize( sessionStore );

    assertTrue( deserializedSession.isBound() );
  }

  public void testListenersAreSerializable() throws Exception {
    SessionStoreListener listener = new LoggingSessionStoreListener();
    sessionStore.addSessionStoreListener( listener );
    SessionStoreImpl deserializedSession = Fixture.serializeAndDeserialize( sessionStore );
    HttpSession newHttpSession = new TestSession();
    deserializedSession.attachHttpSession( newHttpSession );
    SessionStoreImpl.attachInstanceToSession( newHttpSession, deserializedSession );
    newHttpSession.invalidate();

    assertTrue( LoggingSessionStoreListener.wasCalled );
  }

  public void testNonSerializableAttributeCausesException() throws IOException {
    sessionStore.setAttribute( "foo", new Object() );
    try {
      Fixture.serialize( sessionStore );
      fail();
    } catch( NotSerializableException expected ) {
    }
  }

  protected void setUp() throws Exception {
    LoggingSessionStoreListener.wasCalled = false;
    httpSession = new TestSession();
    sessionStore = new SessionStoreImpl( httpSession );
  }
}
