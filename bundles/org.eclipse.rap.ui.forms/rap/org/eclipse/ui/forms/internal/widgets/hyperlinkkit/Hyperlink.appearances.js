/*******************************************************************************
 * Copyright (c) 2007, 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

appearances = {
// BEGIN TEMPLATE //

  "hyperlink" : {
    style : function( states ) {
      var tv = new org.eclipse.swt.theme.ThemeValues( states );
      return {
        font: tv.getCssFont( "*", "font" ),
        cursor : "pointer",
        spacing : 4,
        width : "auto",
        height : "auto",
        horizontalChildrenAlign : "center",
        verticalChildrenAlign : "middle"
      }
    }
  }

// END TEMPLATE //
};
