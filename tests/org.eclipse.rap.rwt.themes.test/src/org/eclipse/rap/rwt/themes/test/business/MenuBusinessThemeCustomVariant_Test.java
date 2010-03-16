/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt.themes.test.business;

import org.eclipse.rap.rwt.themes.test.ThemesTestUtil;
import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.theme.QxBorder;
import org.eclipse.rwt.internal.theme.QxBoxDimensions;
import org.eclipse.rwt.internal.theme.QxColor;
import org.eclipse.rwt.internal.theme.QxType;
import org.eclipse.rwt.internal.theme.SimpleSelector;
import org.eclipse.rwt.internal.theme.ThemeUtil;
import org.eclipse.swt.graphics.Color;

import junit.framework.TestCase;


public class MenuBusinessThemeCustomVariant_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    ThemesTestUtil.activateTheme( ThemesTestUtil.BUSINESS_THEME_ID, 
                                   ThemesTestUtil.BUSINESS_PATH );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
  
  public void testMenuHeaderToolbarColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "3b8fc2", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuHeaderToolbarBackgroundColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "background-color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "ffffff", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuHeaderToolbarBorder() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "border", 
                                             selector );
    QxBorder border = ( QxBorder ) cssValue;
    assertEquals( "solid", border.style );
    assertEquals( 1, border.width );
    assertEquals( "#005499", border.color );
  }
  
  public void testMenuHeaderToolbarBorderRadius() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "border-radius", 
                                             selector );
    QxBoxDimensions dim = ( QxBoxDimensions ) cssValue;
    assertEquals( 0, dim.top );
    assertEquals( 0, dim.right );
    assertEquals( 2, dim.bottom );
    assertEquals( 2, dim.left );
  }
  
  public void testMenuItemHeaderToolbarColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "004277", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuItemHeaderToolbarColorHover() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar", ":hover" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "0369bc", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuItemHeaderToolbarBackgroundColorHover() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-toolbar", ":hover" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "background-color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    assertTrue( color.transparent );
  }
  
  public void testMenuHeaderOveflowColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "656565", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuHeaderOverflowBackgroundColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "background-color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "ffffff", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuHeaderOverflowBorder() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "border", 
                                             selector );
    QxBorder border = ( QxBorder ) cssValue;
    assertEquals( "solid", border.style );
    assertEquals( 1, border.width );
    assertEquals( "#005499", border.color );
  }
  
  public void testMenuHeaderOverflowBorderRadius() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "border-radius", 
                                             selector );
    QxBoxDimensions dim = ( QxBoxDimensions ) cssValue;
    assertEquals( 2, dim.top );
    assertEquals( 2, dim.right );
    assertEquals( 2, dim.bottom );
    assertEquals( 2, dim.left );
  }
  
  public void testMenuItemHeaderOverflowColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "004277", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuItemHeaderOverflowColorHover() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow", ":hover" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "0369bc", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuItemHeaderOverflowBackgroundColorHover() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".header-overflow", ":hover" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "background-color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    assertTrue( color.transparent );
  }
  
  public void testMenuMenuBarColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "0059a5", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuMenuBarBackgroundColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "background-color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "ffffff", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuMenuBarBorder() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "border", 
                                             selector );
    QxBorder border = ( QxBorder ) cssValue;
    assertEquals( "solid", border.style );
    assertEquals( 1, border.width );
    assertEquals( "#cccccc", border.color );
  }
  
  public void testMenuMenuBarBorderRadius() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar" } );
    QxType cssValue = ThemeUtil.getCssValue( "Menu", 
                                             "border-radius", 
                                             selector );
    QxBoxDimensions dim = ( QxBoxDimensions ) cssValue;
    assertEquals( 0, dim.top );
    assertEquals( 0, dim.right );
    assertEquals( 2, dim.bottom );
    assertEquals( 2, dim.left );
  }
  
  public void testMenuItemMenuBarColor() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "004277", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuItemMenuBarColorHover() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar", ":hover" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    Color swtColor = Graphics.getColor( color.red, color.green, color.blue );
    assertEquals( "0369bc", ThemesTestUtil.getHexStringFromColor( swtColor ) );
  }
  
  public void testMenuItemMenuBarBackgroundColorHover() {
    SimpleSelector selector 
      = new SimpleSelector( new String[] { ".menuBar", ":hover" } );
    QxType cssValue = ThemeUtil.getCssValue( "MenuItem", 
                                             "background-color", 
                                             selector );
    QxColor color = ( QxColor ) cssValue;
    assertTrue( color.transparent );
  }
}