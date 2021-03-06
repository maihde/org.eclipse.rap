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
package org.eclipse.rap.rwt.osgi;

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;


/**
 * <strong>Note:</strong> This API is <em>provisional</em>. It is likely to change before the final
 * release.
 *
 * @since 1.5
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ApplicationLauncher {

  public static final String PROPERTY_CONTEXT_NAME = "contextName";

  ApplicationReference launch( ApplicationConfiguration configuration,
                               HttpService httpService,
                               HttpContext httpContext,
                               String contextName,
                               String contextDirectory );

}
