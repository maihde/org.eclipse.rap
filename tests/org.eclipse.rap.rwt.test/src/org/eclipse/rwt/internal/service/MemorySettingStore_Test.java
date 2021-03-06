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
package org.eclipse.rwt.internal.service;

import org.eclipse.rap.rwt.testfixture.internal.service.MemorySettingStore;
import org.eclipse.rap.rwt.testfixture.internal.service.MemorySettingStoreFactory;
import org.eclipse.rwt.service.FileSettingStore_Test;
import org.eclipse.rwt.service.ISettingStoreFactory;


/**
 * Tests for the classes {@link MemorySettingStore} 
 * and {@link MemorySettingStoreFactory}.
 */
public class MemorySettingStore_Test extends FileSettingStore_Test {
  
  private ISettingStoreFactory factory = new MemorySettingStoreFactory();

  protected ISettingStoreFactory getFactory() {
    return factory;
  }
  
  public void testFactoryCreatesRightInstance() {
    String id = getClass().getName();
    assertTrue( factory.createSettingStore( id ) instanceof MemorySettingStore );
  }

}
