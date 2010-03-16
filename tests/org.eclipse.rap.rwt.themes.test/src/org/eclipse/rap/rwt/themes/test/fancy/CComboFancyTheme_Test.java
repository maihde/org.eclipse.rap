/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt.themes.test.fancy;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.themes.test.ThemesTestUtil;
import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.theme.QxBorder;
import org.eclipse.rwt.internal.theme.QxBoxDimensions;
import org.eclipse.rwt.internal.theme.QxColor;
import org.eclipse.rwt.internal.theme.QxDimension;
import org.eclipse.rwt.internal.theme.QxFont;
import org.eclipse.rwt.internal.theme.QxImage;
import org.eclipse.rwt.internal.theme.QxType;
import org.eclipse.rwt.internal.theme.SimpleSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class CComboFancyTheme_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    ThemesTestUtil.activateTheme( ThemesTestUtil.FANCY_THEME_ID, 
                                   ThemesTestUtil.FANCY_PATH );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
  
  public void testComboColor() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue = ThemesTestUtil.getCssValue( combo, 
                                                   selector, 
                                                   "color" );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "4a4a4a", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testComboBackgroundColor() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue = ThemesTestUtil.getCssValue( combo, 
                                                   selector, 
                                                   "background-color" );
    QxColor color = ( QxColor ) cssValue;
    assertEquals( 252, color.red );
    assertEquals( 252, color.green );
    assertEquals( 252, color.blue );
  }
  
  public void testComboFont() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue = ThemesTestUtil.getCssValue( combo, 
                                                   selector, 
                                                   "font" );
    QxFont font = ( QxFont ) cssValue;
    String[] family = font.family;
    assertEquals( 5, family.length );
    assertEquals( "Verdana", family[ 0 ] );
    assertEquals( "Lucida Sans", family[ 1 ] );
    assertEquals( "Arial", family[ 2 ] );
    assertEquals( "Helvetica", family[ 3 ] );
    assertEquals( "sans-serif", family[ 4 ] );
    assertEquals( 11, font.size );
    assertFalse( font.bold );
  }
  
  public void testComboBorderBorder() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = new SimpleSelector( new String[] { "[BORDER" } );
    QxType cssValue = ThemesTestUtil.getCssValue( combo, 
                                                   selector, 
                                                   "border" );
    QxBorder border = ( QxBorder ) cssValue;
    assertEquals( 1, border.width );
    assertEquals( "#c1c1c1", border.color );
    assertEquals( "solid", border.style );
  }
  
  public void testComboBorderRadiusBorder() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = new SimpleSelector( new String[] { "[BORDER" } );
    QxType cssValue = ThemesTestUtil.getCssValue( combo, 
                                                   selector, 
                                                   "border-radius" );
    QxBoxDimensions dim = ( QxBoxDimensions ) cssValue;
    assertEquals( 2, dim.bottom );
    assertEquals( 2, dim.top );
    assertEquals( 2, dim.left );
    assertEquals( 2, dim.right );
  }
  
  public void testComboButtonIconBackroundImage() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "background-image",
                                               "CCombo-Button-Icon");
    QxImage image = ( QxImage ) cssValue;
    assertEquals( "theme/fancy/icons/down.png", image.path );
  }
  
  public void testComboButtonIconBackroundImageHover() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = new SimpleSelector( new String[] { ":hover" } );
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "background-image",
                                               "CCombo-Button-Icon");
    QxImage image = ( QxImage ) cssValue;
    assertEquals( "theme/fancy/icons/down-hover.png", image.path );
  }
  
  public void testComboButtonBackgroundColor() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "background-color",
                                               "CCombo-Button" );
    QxColor color = ( QxColor ) cssValue;
    assertTrue( color.transparent );
  }
  
  public void testComboButtonBorder() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "border",
                                               "CCombo-Button" );
    QxBorder border = ( QxBorder ) cssValue;
    assertEquals( QxBorder.NONE, border );
  }
  
  public void testComboButtonBorderRadius() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "border-radius",
                                               "CCombo-Button" );
    QxBoxDimensions dim = ( QxBoxDimensions ) cssValue;
    assertEquals( 2, dim.bottom );
    assertEquals( 0, dim.top );
    assertEquals( 0, dim.left );
    assertEquals( 2, dim.right );
  }
  
  public void testComboButtonWidth() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "width",
                                               "CCombo-Button" );
    QxDimension dim = ( QxDimension ) cssValue;
    assertEquals( 18, dim.value );
  }
  
  public void testComboButtonBackgroundImage() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "background-image",
                                               "CCombo-Button" );
    QxImage image = ( QxImage ) cssValue;
    String[] colors = image.gradientColors;
    float[] percents = image.gradientPercents;
    assertEquals( 4, colors.length );
    assertEquals( 4, percents.length );
    assertEquals( "#ffffff", colors[ 0 ] );
    assertEquals( "#f0f0f0", colors[ 1 ] );
    assertEquals( "#e0e0e0", colors[ 2 ] );
    assertEquals( "#cccccc", colors[ 3 ] );
    assertEquals( 0.0, percents[ 0 ], 0.0 );
    assertEquals( 48.0, percents[ 1 ], 0.0 );
    assertEquals( 52.0, percents[ 2 ], 0.0 );
    assertEquals( 100.0, percents[ 3 ], 0.0 );
  }
  
  public void testComboButtonBackgroundImagePressed() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = new SimpleSelector( new String[] { ":pressed" } );
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "background-image",
                                               "CCombo-Button" );
    QxImage image = ( QxImage ) cssValue;
    String[] colors = image.gradientColors;
    float[] percents = image.gradientPercents;
    assertEquals( 3, colors.length );
    assertEquals( 3, percents.length );
    assertEquals( "#e0e0e0", colors[ 0 ] );
    assertEquals( "#e0e0e0", colors[ 1 ] );
    assertEquals( "#b0b0b0", colors[ 2 ] );
    assertEquals( 0.0, percents[ 0 ], 0.0 );
    assertEquals( 52.0, percents[ 1 ], 0.0 );
    assertEquals( 100.0, percents[ 2 ], 0.0 );
  }
  
  public void testComboListBorder() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "border",
                                               "CCombo-List" );
    QxBorder border = ( QxBorder ) cssValue;
    assertEquals( "solid", border.style );
    assertEquals( 1, border.width );
    assertEquals( "#c1c1c1", border.color );
  }
  
  public void testComboListBorderRadius() {
    CCombo combo = createCombo( SWT.NONE );
    SimpleSelector selector = SimpleSelector.DEFAULT;
    QxType cssValue 
      = ThemesTestUtil.getCssValueForElement( combo, 
                                               selector, 
                                               "border-radius",
                                               "CCombo-List" );
    QxBoxDimensions dim = ( QxBoxDimensions ) cssValue;
    assertEquals( 2, dim.bottom );
    assertEquals( 0, dim.top );
    assertEquals( 2, dim.left );
    assertEquals( 0, dim.right );
  }
      
  /*
   * Little Helper Method to create CCombos
   */
  private CCombo createCombo( final int style ) {
    Display display = new Display();
    Shell shell = new Shell( display, style );
    CCombo combo = new CCombo( shell, style );
    for( int i = 0; i < 10; i++ ) {
      combo.add( "Comboitem " + i );
    }
    return combo;
  }
  
}