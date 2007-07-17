/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.internal.widgets.textkit;

import org.eclipse.rap.swt.theme.IControlThemeAdapter;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;


public interface ITextThemeAdapter extends IControlThemeAdapter {

  public Rectangle getPadding( final Control control );

}
