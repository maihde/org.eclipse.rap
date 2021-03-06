/*******************************************************************************
 * Copyright (c) 2011 Frank Appel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Frank Appel - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.rwt.osgi.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osgi.framework.*;
import org.osgi.service.http.HttpService;


public class HttpTracker_Test extends TestCase {

  private BundleContext bundleContext;
  private ApplicationLauncherImpl applicationLauncher;
  private ServiceReference< HttpService> serviceReference;
  private HttpService service;
  private HttpTracker tracker;

  public void testAddingService() {
    tracker.addingService( serviceReference );
    
    verify( applicationLauncher ).addHttpService( serviceReference );
  }
  
  public void testRemovedService() {
    tracker.removedService( serviceReference, service );
    
    verify( applicationLauncher ).removeHttpService( service );
  }
  
  public void testOpen() {
    tracker.open();
    
    verify( applicationLauncher ).addHttpService( serviceReference );
  }
  
  @SuppressWarnings( "unchecked" )
  protected void setUp() throws Exception {
    mockBundleContext();
    applicationLauncher = mock( ApplicationLauncherImpl.class );
    tracker = new HttpTracker( bundleContext, applicationLauncher );
    serviceReference = mock( ServiceReference.class );
    service = mock( HttpService.class );
  }

  private void mockBundleContext() throws InvalidSyntaxException {
    bundleContext = mock( BundleContext.class );
    doAnswer( createServiceRegistrationTrigger() )
     .when( bundleContext ).addServiceListener( any( ServiceListener.class ), any( String.class ) );
  }

  private Answer createServiceRegistrationTrigger() {
    return new Answer() {
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        triggerServiceRegistration( invocation );
        return null;
      }
    };
  }

  private void triggerServiceRegistration( InvocationOnMock invocation ) {
    ServiceListener listener = ( ServiceListener )invocation.getArguments()[ 0 ];
    listener.serviceChanged( new ServiceEvent( ServiceEvent.REGISTERED, serviceReference ) );
  }
}
