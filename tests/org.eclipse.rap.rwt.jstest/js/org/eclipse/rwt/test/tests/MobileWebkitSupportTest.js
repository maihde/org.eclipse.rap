/*******************************************************************************
 * Copyright (c) 2010, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 *    Austin Riddle (Texas Center for Applied Technology) - tests for draggable types
 ******************************************************************************/

qx.Class.define( "org.eclipse.rwt.test.tests.MobileWebkitSupportTest", {

  extend : qx.core.Object,

  construct : function() {
    // Eventlistener are detached by TestRunner to prevent user-interference,
    // but we need them here...
    org.eclipse.rwt.EventHandler.attachEvents();
    if( org.eclipse.rwt.Client.isAndroidBrowser() ) {
      org.eclipse.rwt.MobileWebkitSupport._getTouch = function( event ) {
        // touches is always null on faked TouchEvent, use fakedTouches instead
        var touches = event.touches || event.fakeTouches;
        var touch = touches.item( 0 );
        if( touch === null ) {
          touch = event.changedTouches.item( 0 );
        }
        return touch;
      };
    }
  },
  
  destruct : function() {
    org.eclipse.rwt.EventHandler.detachEvents();
  },

  members : {

    TARGETENGINE : [ "webkit" ],
    TARGETPLATFORM : [ "ios", "android" ],
    
    ///////////////
    // Test helpers

    testCreateTouch : function() {
      var div = document.createElement( "div" );
      var touch = this.createTouch( div, 3, 6 );
      assertEquals( 3, touch.screenX );
      assertEquals( 6, touch.screenY );
      assertIdentical( div, touch.target );
    },
    
    testCreateTouchEvent : function() {
      var touches = [ 
        this.createTouch( document.body, 3, 6 ), 
        this.createTouch( document.body, 4, 6 ) 
      ];      
      var list = this.createTouchList( touches );
      var event = this.createTouchEvent( "touchstart", list );
      assertTrue( "touchstart" === event.type );
      assertTrue( list === event.touches || list === event.fakeTouches );
    },

    testCreateTouchList : function() {
      var div = document.createElement( "div" );
      var touches = [ 
        this.createTouch( div, 3, 6 ), 
        this.createTouch( div, 4, 6 ) 
      ];
      var list = this.createTouchList( touches );
      assertEquals( 2, list.length );
      assertIdentical( touches[ 0 ], list.item( 0 ) );
      assertIdentical( touches[ 1 ], list.item( 1 ) );
    },

    testFakeTouchEvents : function() {
      var div = document.createElement( "div" );
      document.body.appendChild( div );
      log = [];
      var logger = function( event ) {
        log.push( event.type );
      };
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart" );
      this.touch( div, "touchmove" );
      this.touch( div, "touchend" );
      this.touch( div, "touchcancel" );
      var expected = [ "touchstart", "touchmove", "touchend", "touchcancel" ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },
    
    testFakeTouchEventsTargets : function() {
      var div = document.createElement( "div" );
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.target );
      };
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart" );
      this.touch( div, "touchmove" );
      this.touch( div, "touchend" );
      this.touch( div, "touchcancel" );
      var expected = [ div, div, div, div ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },

    testFakeTouchEventsTouchNumber : function() {
      var div = document.createElement( "div" );
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        var touches = event.fakeTouches || event.touches; 
        log.push( touches.length );
      };
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart", 1 );
      this.touch( div, "touchmove", 1 );
      this.touch( div, "touchend", 3 );
      this.touch( div, "touchcancel",4  );
      var expected = [ 1, 1, 3, 4 ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },

    testFakeTouchEventsTouchList : function() {
      var div = document.createElement( "div" );
      var touches = [ 
        this.createTouch( div, 1, 1 ), 
        this.createTouch( div, 1, 1 )
      ]; 
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        var touches = event.fakeTouches || event.touches;
        log.push( touches );
      };
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart", touches );
      assertEquals( 2, log[ 0 ].length );
      assertIdentical( touches[ 0 ], log[ 0 ].item( 0 ) );
      assertIdentical( touches[ 1 ], log[ 0 ].item( 1 ) );
      document.body.removeChild( div );
    },

    testFakeGestureEvent : function() {
      if( !org.eclipse.rwt.Client.isAndroidBrowser() ) { // android can't fake gestures at all
        var div = document.createElement( "div" );
        document.body.appendChild( div );
        var log = [];
        var logger = function( event ) {
          log.push( event.type );
        };
        div.ontouchstart = logger;  
        div.ontouchmove = logger;
        div.ontouchend = logger;
        div.ontouchcancel = logger;
        div.ongesturestart = logger;
        div.ongesturechange = logger;
        div.ongestureend = logger;
        this.gesture( div, "gesturestart" );
        this.gesture( div, "gesturechange" );
        this.gesture( div, "gestureend" );
        var expected = [
          "touchstart",
          "gesturestart",
          "touchstart",
          "gesturechange",
          "touchmove",
          "gestureend",
          "touchend"
        ];
        assertEquals( expected, log );
        document.body.removeChild( div );
      }
    },

    testFakeGestureEventTouch : function() {
      if( !org.eclipse.rwt.Client.isAndroidBrowser() ) {
        var div = document.createElement( "div" );
        var touches = [ new Touch(), new Touch() ];
        document.body.appendChild( div );
        var log = [];
        var logger = function( event ) {
          log.push( event.touches.length );
        };
        div.ontouchstart = logger;  
        div.ontouchmove = logger;
        div.ontouchend = logger;
        this.gesture( div, "gesturestart" );
        this.gesture( div, "gesturechange" );
        this.gesture( div, "gestureend" );
        var expected = [ 1, 2, 2, 2 ];
        assertEquals( expected, log );
        document.body.removeChild( div );
      }
    },

    //////////
    // Visuals

    testTabHighlightHidden : function() {
      var head = document.head;
      var headertext = head.innerHTML;
      var expected = "* { -webkit-tap-highlight-color: rgba(0,0,0,0); }";
      assertTrue( headertext.indexOf( expected ) != -1 );
    },
    
    //////////
    // Widgets
    
    // See Bug 323803 -  [ipad] Browser-widget/iframe broken  
    testIFrameDimensionBug : function() {
      if( org.eclipse.rwt.Client.isMobileSafari() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var iframe = new qx.ui.embed.Iframe();
        iframe.addToDocument();
        iframe.setWidth( 300 );
        iframe.setHeight( 400 );
        TestUtil.flush();
        var node = iframe.getIframeNode();
        var widgetNode = iframe.getElement();
        assertEquals( 300, parseInt( widgetNode.style.width, 10 ) );
        assertEquals( 400, parseInt( widgetNode.style.height, 10 ) );
        assertEquals( "", node.width );
        assertEquals( "", node.height );
        assertEquals( "", node.style.width );
        assertEquals( "", node.style.height );
        assertEquals( "300px", node.style.minWidth );
        assertEquals( "400px", node.style.minHeight );
        assertEquals( "300px", node.style.maxWidth );
        assertEquals( "400px", node.style.maxHeight );
      }
    },
    
    testTextFocusIOS : function() {
      if( org.eclipse.rwt.Client.isMobileSafari() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var text = new org.eclipse.rwt.widgets.Text( false );
        text.addToDocument();
        TestUtil.flush();
        assertFalse( text.isFocused() );
        var node = text._inputElement;
        var over = false;
        text.addEventListener( "mouseover", function(){
          over = true;
        } );
        
        this.touch( node, "touchstart" );
        TestUtil.fakeMouseEventDOM( node, "mouseover", 1, 0, 0, 0, true ); // fakes "native" event
        if( !over ) {
          // the ipad will only send a mousedown if mouseover is not processed
          TestUtil.fakeMouseEventDOM( node, "mousedown", 1, 0, 0, 0, true ); 
        }      
  
        assertTrue( text.isFocused() );
      }
    },
    
    testTextFocusAndroid : function() {
      if( org.eclipse.rwt.Client.isAndroidBrowser() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var text = new org.eclipse.rwt.widgets.Text( false );
        text.addToDocument();
        TestUtil.flush();
        var node = text._inputElement;
        var log = [];
        
        log.push( this.touch( node, "touchstart" ) );
        log.push( this.touch( node, "touchend" ) );

        assertEquals( [ true, false ], log );
        assertTrue( text.isFocused() );
      }
    },


    /////////
    // Events
    
    testPreventNativeMouseEvents : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();
      var counter = 0;
      widget.addEventListener( "mousedown", function(){ counter++; } );
      var node = widget._getTargetNode();
      TestUtil.fakeMouseEventDOM( node, "mousedown", 1, 0, 0, 0, true );
      assertEquals( 0, counter );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testAllowNativeMouseWheelEvents : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();
      var counter = 0;
      widget.addEventListener( "mousewheel", function(){ counter++; } );
      var node = widget._getTargetNode();
      TestUtil.fakeMouseEventDOM( node, "mousewheel", 1, 0, 0, 0, true );
      assertEquals( 1, counter );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },

    testTouchStartCreatesMouseDown : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      assertEquals( [ "mousedown" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testTouchIgnoredWhileInReponse : function() { // See Bug 379140
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      var node = widget._getTargetNode();
      
      org.eclipse.swt.EventUtil.setSuspended( true );
      this.touch( node, "touchstart" );
      org.eclipse.swt.EventUtil.setSuspended( false );
      
      assertEquals( [], log ); // should also call prevent default, but that cant be tested
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testTouchEndCreatesMouseUp : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mouseup", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mouseup" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testTouchToolTips : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var toolTip = org.eclipse.rwt.widgets.WidgetToolTip.getInstance();
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      widget.setToolTip( toolTip );
      widget.setUserData( "toolTipText", "foo" );
      TestUtil.flush();
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      TestUtil.forceInterval( toolTip._showTimer );
      assertTrue( toolTip.getVisibility() );
      this.touch( node, "touchend" );
      assertFalse( toolTip.getVisibility() );
      this.touch( node, "touchstart" );
      TestUtil.forceInterval( toolTip._showTimer );
      assertTrue( toolTip.getVisibility() );
      this.touch( node, "touchend" );
      assertFalse( toolTip.getVisibility() );
      widget.destroy();
      this.resetMobileWebkitSupport();

    },
    
    testMouseOver : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mouseover", logger );
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mouseover" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testMouseOut : function() {
      this.resetMobileWebkitSupport();
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [], log );
      this.touch( document.body, "touchstart" );
      assertEquals( [ "mouseout" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testCoordinatesMouseDownUp : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getPageX(), event.getPageY() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      var node = widget._getTargetNode(); 
      this.touchAt( node, "touchstart", 1, 2 );
      this.touchAt( node, "touchend", 3, 4 );
      assertEquals( [ 1, 2, 3, 4 ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testPreventTapZoom : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();
      var prevented = false; 
      widget.addEventListener( "mouseup", function( event ) {
        prevented = event.getDomEvent().originalEvent.prevented === true;
      } );
      var node = widget._getTargetNode();
      this.resetMobileWebkitSupport();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertTrue( prevented );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },

    testClick : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mousedown", "mouseup", "click"], log );
      widget.destroy();          
      this.resetMobileWebkitSupport();
    },
    
    testNoClickOnDifferentTargets : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      var node = widget._getTargetNode();
      this.touch( document.body, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mouseup" ], log );
      widget.destroy();      
      this.resetMobileWebkitSupport();
    },
    
    testIsDraggableShell : function() {
      var widget = new org.eclipse.swt.widgets.Shell( {} );
      widget.addToDocument();
      widget.initialize();
      widget.open();
      assertTrue( this._isDraggable( widget ) );
      assertTrue( this._isDraggable( widget ) );
      widget.destroy();
    },
    
    testIsDraggableSash : function() {
      var widget = new org.eclipse.swt.widgets.Sash();
      widget.addToDocument();
      assertTrue( this._isDraggable( widget ) );
      widget.destroy();
    },
    
    testIsDraggableScale : function() {
      var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
      var widget = new org.eclipse.swt.widgets.Scale( "horizontal" );
      widget.addToDocument();
      widgetManager.add( widget, "scale", true );
      widgetManager.add( widget._thumb, "scale-thumb", false );
      assertTrue( this._isDraggable( widget._thumb ) );
      widget.destroy();
    },
      
    testIsDraggableSlider : function() {
      var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
      var widget = new org.eclipse.swt.widgets.Slider( "horizontal" );
      widget.addToDocument();
      widgetManager.add( widget, "slider", true );
      widgetManager.add( widget._thumb, "slider-thumb", false );
      assertTrue( this._isDraggable( widget._thumb ) );
      widget.destroy();
    },
    
    testIsDraggableScrollBar : function() {
      var widget = new org.eclipse.rwt.widgets.ScrollBar( false );
      widget.addToDocument();
      assertTrue( this._isDraggable( widget ) );
      widget.destroy();
    },
    
    testIsDraggableScrolledComposite : function() {
      var widget = new org.eclipse.swt.custom.ScrolledComposite( false );
      widget.addToDocument();
      assertTrue( this._isDraggable( widget._horzScrollBar._thumb ) );
      widget.destroy();
    },
      
    testIsDraggableComboScrollBar : function() {
      var widget = new org.eclipse.rwt.widgets.BasicButton( "push" );
      widget.setAppearance( "scrollbar-thumb" );
      widget.addToDocument();
      assertTrue( this._isDraggable( widget ) );
      widget.destroy();
    },
      
    testIsDraggableCoolItem : function() {
      var widget = new qx.ui.layout.CanvasLayout();
      widget.setAppearance( "coolitem-handle" );
      widget.addToDocument();
      assertTrue( this._isDraggable( widget ) );
      widget.destroy();
    },
    
    testIsDraggableList : function() {
      var widget = new org.eclipse.swt.widgets.List( true );
      widget.addToDocument();
      assertTrue( this._isDraggable( widget._horzScrollBar._thumb ) );
      assertTrue( this._isDraggable( widget._vertScrollBar._thumb ) );
      widget.destroy();
    },
    

    testIsDraggableTreeScrollBarAndColumn : function() {
      var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
      var widget = new org.eclipse.rwt.widgets.Tree( {
        appearance : "tree"
      } );
      widget.addToDocument();
      widgetManager.add( widget, "tree", true );
      var column = new org.eclipse.swt.widgets.TableColumn( widget );
      column.setLabel( "test" );
      column.setIcon( "http://blah.blah" );
      column.setParent( widget );
      widgetManager.add( column, "tree-column", false );
      assertTrue( this._isDraggable( column ) );
      assertTrue( this._isDraggable( column.getLabelObject() ) );
      assertTrue( this._isDraggable( column._iconObject ) );
      assertTrue( this._isDraggable( widget._horzScrollBar._thumb ) );
      assertTrue( this._isDraggable( widget._vertScrollBar._thumb ) );
      widget.destroy();
         
      this.resetMobileWebkitSupport();
    },
        
    testIsNotDraggableWidget : function() {
      //test basic widgets without proper appearance that might allow a broader match
      var widget = new org.eclipse.rwt.widgets.BasicButton( "push" );
      widget.addToDocument();
      assertFalse( this._isDraggable( widget ) );
      widget.destroy();
      
      widget = new qx.ui.layout.CanvasLayout();
      widget.addToDocument();
      assertFalse( this._isDraggable( widget ) );
      widget.destroy();
      
      this.resetMobileWebkitSupport();
    },
    
    testCancelOnGesture : function() {
      if( !org.eclipse.rwt.Client.isAndroidBrowser() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var doc = qx.ui.core.ClientDocument.getInstance();
        var widget = new qx.ui.basic.Terminator();
        widget.addToDocument();
        TestUtil.flush();      
        var widgetLog = [];
        var widgetLogger = function( event ){ 
          widgetLog.push( event.getType() );
          event.stopPropagation(); 
        };
        var docLog = [];
        var docLogger = function( event ){ 
          docLog.push( event.getType() ); 
        };
        widget.addEventListener( "mouseover", widgetLogger );
        widget.addEventListener( "mouseout", widgetLogger );
        widget.addEventListener( "mousedown", widgetLogger );
        widget.addEventListener( "mouseup", widgetLogger );
        widget.addEventListener( "click", widgetLogger );
        doc.addEventListener( "mouseover", docLogger );
        doc.addEventListener( "mouseout", docLogger );
        doc.addEventListener( "mousedown", docLogger );
        doc.addEventListener( "mouseup", docLogger );
        doc.addEventListener( "click", docLogger );
        var node = widget._getTargetNode();
        this.gesture( node, "gesturestart" );
        this.touch( node, "touchstart", 3 );
        this.touch( node, "touchend", 2 );
        this.gesture( node, "gestureend" );
        var widgetExpected = [ "mouseover", "mousedown", "mouseout" ];
        var docExpected = [ "mouseover", "mouseup" ];      
        assertEquals( widgetExpected, widgetLog );
        assertEquals( docExpected, docLog );
        widget.destroy();
        this.resetMobileWebkitSupport();
      }
    },
    
    testCancelOnSwipe : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var doc = qx.ui.core.ClientDocument.getInstance();
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var widgetLog = [];
      var widgetLogger = function( event ){ 
        widgetLog.push( event.getType() );
        event.stopPropagation(); 
      };
      var docLog = [];
      var docLogger = function( event ){ 
        docLog.push( event.getType() ); 
      };
      widget.addEventListener( "mouseover", widgetLogger );
      widget.addEventListener( "mouseout", widgetLogger );
      widget.addEventListener( "mousedown", widgetLogger );
      widget.addEventListener( "mouseup", widgetLogger );
      widget.addEventListener( "click", widgetLogger );
      doc.addEventListener( "mouseover", docLogger );
      doc.addEventListener( "mouseout", docLogger );
      doc.addEventListener( "mousedown", docLogger );
      doc.addEventListener( "mouseup", docLogger );
      doc.addEventListener( "click", docLogger );
      var node = widget._getTargetNode();
      this.touchAt( node, "touchstart", 10, 10 );
      assertEquals( 2, widgetLog.length );
      this.touchAt( node, "touchmove", 24, 24 );
      assertEquals( 2, widgetLog.length );
      this.touchAt( node, "touchmove", 25, 25 );
      this.touch( node, "touchend" );
      var widgetExpected = [ "mouseover", "mousedown", "mouseout" ];
      var docExpected = [ "mouseover", "mouseup" ];      
      assertEquals( widgetExpected, widgetLog );
      assertEquals( docExpected, docLog );
      widgetLog = [];
      docLog = [];
      this.touch( node, "touchstart" );
      widgetExpected = [ "mouseover", "mousedown" ];
      docExpected = [ "mouseout" ];      
      assertEquals( widgetExpected, widgetLog );
      assertEquals( docExpected, docLog );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testDoubleClick : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      widget.addEventListener( "dblclick", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      var expected = [
        "mousedown", 
        "mouseup", 
        "click",
        "mousedown", 
        "mouseup", 
        "click",
        "dblclick"
      ];
      assertEquals( expected, log );
      widget.destroy();                
      this.resetMobileWebkitSupport();
    },
    
    testNoDoubeClickDifferentTarget : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      widget.addEventListener( "dblclick", logger );
      var node = widget._getTargetNode();
      this.touch( document.body, "touchstart" );
      this.touch( document.body, "touchend" );
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      var expected = [ "mousedown", "mouseup", "click" ];
      assertEquals( expected, log );
      widget.destroy();      
      this.resetMobileWebkitSupport();
    },

    testNoDoubleClickTooSlow : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      widget.addEventListener( "dblclick", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      org.eclipse.rwt.MobileWebkitSupport._lastMouseClickTime -= 1000;
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      var expected = [ "mousedown", "mouseup", "click", "mousedown", "mouseup", "click" ];
      assertEquals( expected, log );
      widget.destroy();      
      this.resetMobileWebkitSupport();
    },
    
    testPreventDefaultAtFullscreen : function() {
      this.fakeFullscreen();
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();
      var log = [];
      var logger = function( event ) {
        log.push( event.getDomEvent().originalEvent.prevented );
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      this.touchAt( node, "touchstart", 10, 10 );
      this.touchAt( node, "touchmove", 25, 25 );
      var expected = [ true, true, true, true ];
      assertEquals( expected, log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testPreventDefaultOnSwipe : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      TestUtil.flush();
      var log = [];
      var logger = function( event ) {
        //log.push( event.getDomEvent().originalEvent );
        log.push( event );
      };
      //widget.addEventListener( "mouseout", logger );
      document.body.addEventListener( "touchstart", logger );
      document.body.addEventListener( "touchmove", logger );
      var node = widget._getTargetNode();
      this.touchAt( node, "touchstart", 0, 0  );
      this.touchAt( node, "touchmove", 19, 19 );
      assertTrue( log[ 0 ].prevented );
      assertTrue( log[ 1 ].prevented );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },

    testAllowSwipeOnScrollable : function() {
      if( !org.eclipse.rwt.Client.isAndroidBrowser() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var widget = new org.eclipse.swt.custom.ScrolledComposite();
        widget.addToDocument();
        TestUtil.flush();
        var log = [];
        var logger = function( event ) {
          //log.push( event.getDomEvent().originalEvent );
          log.push( event );
        };
        //widget._clientArea.addEventListener( "mouseout", logger );
        document.body.addEventListener( "touchstart", logger );
        document.body.addEventListener( "touchmove", logger );
        var node = widget._clientArea._getTargetNode();
        this.touchAt( node, "touchstart", 0, 0  );
        this.touchAt( node, "touchmove", 19, 19 );
        assertFalse( log[ 0 ].prevented );
        assertFalse( log[ 1 ].prevented );
        widget.destroy();
        this.resetMobileWebkitSupport();
      }
    },
    
    testAllowSwipeOnScrollableChild : function() {
      if( !org.eclipse.rwt.Client.isAndroidBrowser() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var parent = new org.eclipse.swt.custom.ScrolledComposite();
        var widget = new qx.ui.basic.Terminator();
        parent.setContent( widget );
        parent.addToDocument();
        TestUtil.flush();
        var log = [];
        var logger = function( event ) {
          //log.push( event.getDomEvent().originalEvent );
          log.push( event );
        };
        //widget._clientArea.addEventListener( "mouseout", logger );
        document.body.addEventListener( "touchstart", logger );
        document.body.addEventListener( "touchmove", logger );
        var node = widget._getTargetNode();
        this.touchAt( node, "touchstart", 0, 0  );
        this.touchAt( node, "touchmove", 19, 19 );
        assertFalse( log[ 0 ].prevented );
        assertFalse( log[ 1 ].prevented );
        widget.destroy();
        this.resetMobileWebkitSupport();
      }
    },

    testDoNotBlockScrolling : function() {
      if( !org.eclipse.rwt.Client.isAndroidBrowser() ) {
        var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
        var composite = new org.eclipse.swt.custom.ScrolledComposite();
        composite.setLeft( 10 );
        composite.setTop( 10 );
        composite.setWidth( 100 );
        composite.setHeight( 100 );
        composite.addToDocument();
        var area = new org.eclipse.swt.widgets.Composite();
        area.setLeft( 0 );
        area.setTop( 0 );
        composite.setContent( area );
        area.setWidth( 200 );
        area.setHeight( 200 );
        TestUtil.prepareTimerUse();
        var child = new qx.ui.basic.Terminator();
        child.setParent( area );
        child.setLeft( 0 );
        child.setTop( 0 );
        TestUtil.flush();
        composite.setHBarSelection( 10 );
        composite.setVBarSelection( 20 );
  
        child.focus(); // this usually blocks scrolling
        this.touch( area._getTargetNode(), "touchstart" ); // this makes is a swipe-scrolling
        composite._clientArea.setScrollLeft( 50 );
        composite._clientArea.setScrollTop( 70 );
        composite._onscroll( {} );
  
        TestUtil.forceTimerOnce();
        var client = composite._clientArea;
        var position = [ client.getScrollLeft(), client.getScrollTop() ];
        assertEquals( [ 50, 70 ], position );      
        composite.destroy();
      }
    },

    /////////
    // Helper
    
    createTouch : function( target, x, y ) {
      // TODO [tb] : identifier, page vs. screen
      var result = document.createTouch( window, //view
                                         target,
                                         1, //identifier
                                         x, //pageX,
                                         y, //pageY,
                                         x, //screenX,
                                         y //screenY
      );
      return result;
    },
    
    createTouchList : function( touches ) {
      var result;
      if( org.eclipse.rwt.Client.isAndroidBrowser() ) {
        // "real" TouchList does not work propperly (length is always 0)
        result = {};
        result.length = touches.length;
        result.item = function( offset ) {
          return touches[ offset ];
        };
      } else {
        var args = [];
        for( var i = 0; i < touches.length; i++ ) {
          args.push( "touches[ " + i + "]" );
        }
        result = eval( "document.createTouchList(" + args.join() + ")" );
      }
      return result;
    },
    
    createTouchEvent : function( type, touchList ) {
      // Note: the screen/client values are not used in real touch-events.
      var result = document.createEvent( "TouchEvent" );
      if( org.eclipse.rwt.Client.isAndroidBrowser() ) {
        result.initTouchEvent(
            touchList, 
            touchList, 
            touchList, 
            type, 
            window, 
            0, 
            0, 
            0, 
            0, 
            true, 
            false, 
            false, 
            false
          );
        result.fakeTouches = touchList; // touches does not work and can not be overwritten
      } else {
        result.initTouchEvent(
            type,
            true, //canBubble
            true, //cancelable
            window, //view
            0, //detail
            0, //screenX
            0, //screenY
            0, //clientX
            0, //clientY
            false, //ctrlKey
            false, //altKey
            false, //shiftKey
            false, //metaKey
            touchList, //touches
            touchList, //targetTouches
            touchList, //changedTouches
            0, //scale
            0 //rotation
        );
        result.touches = touchList;
      }
      // So we can test if preventDefault has been called:
      result.preventDefault = function() {
        this.prevented = true;
      };
      result.prevented = false;      
      return result;
    },
    
    createGestureEvent : function( type, target ) {
      // Note: the screen/client values are not used in real touch-events.
      var result = document.createEvent( "GestureEvent" ); // not supported in Android (yet)
      result.initGestureEvent(
        type,
        true, // canBubble,
        true, // cancelable
        window, // view
        0, // detail
        0, // screenX
        0, // screenY
        0, // clientX
        0, // clientY
        false, // ctrlKey
        false, // boolean altKey
        false, // shiftKey
        false, // metaKey
        target,
        0, // scale,
        0 // rotation
      );
      return result;
    },
    
    // Some nodes "swallow" (non-fake) touch-events;
    _isValidTouchTarget : function( node ) {
      var result = true;
      if( org.eclipse.rwt.Client.isMobileSafari() ) {
        var tag = node.tagName;
        result = ( tag != "INPUT" && tag != "TEXTAREA");
      }
      return result;
    },

    _isDraggable : function ( widget ) {
      org.eclipse.rwt.test.fixture.TestUtil.flush();
      var node = widget._getTargetNode();
      if( node == null ) {
        throw new Error( "expected non-null node to test" );
      }
      return org.eclipse.rwt.MobileWebkitSupport._isDraggableWidget( node );
    },

    touch : function( node, type, touchesNumberOrArray ) {
      var touches;
      if( touchesNumberOrArray instanceof Array ) {
        touches = touchesNumberOrArray;
      } else {
        touches = []; 
        var number = 1;
        if( typeof touchesNumberOrArray === "number" ) {
          number = touchesNumberOrArray;
        }
        while( touches.length < number ) {
          touches.push( this.createTouch( node, 0, 0 ) );
        }
      }
      var touchList = this.createTouchList( touches );
      var event = this.createTouchEvent( type, touchList );
      if( this._isValidTouchTarget( node ) ) {
        node.dispatchEvent( event );
      }
      return !event.prevented;
    },
    
    gesture : function( node, type ) {
      var event = this.createGestureEvent( type, node );
      var touchType = "";
      switch( type ) {
        case "gesturestart":
          touchType = "touchstart";
        break;
        case "gesturechange":
          touchType = "touchmove";
        break;
        case "gestureend":
          touchType = "touchend";
        break;
      }
      if( type === "gesturestart" ) {
        this.touch( node, touchType, 1 );
      }
      node.dispatchEvent( event );
      this.touch( node, touchType, 2 );
      // NOTE: there should actually be two "touchend" (unless the user
      // raises both finger exactly at once), but due to a bug webkit always
      // reports all touches to have ended, even if only one of several ended.
    },
    
    touchAt : function( node, type, x, y ) {
      this.touch( node, type, [ this.createTouch( node, x, y ) ] );
    },
    
    fakeFullscreen : function() {
      org.eclipse.rwt.MobileWebkitSupport._fullscreen = true;
    },
    
    fakeZoom : function( value ) {
      org.eclipse.rwt.MobileWebkitSupport._isZoomed = function(){
        return value;
      };
    },
    
    resetMobileWebkitSupport : function() {
      var mobile = org.eclipse.rwt.MobileWebkitSupport;
      mobile._lastMouseOverTarget = null;
      mobile._lastMouseDownTarget = null;
      mobile._lastMouseDownPosition = null;
      mobile._lastMouseClickTarget = null;
      mobile._lastMouseClickTime = null;
      mobile._mouseEnabled = true;
      mobile._fullscreen = window.navigator.standalone;
    }

  }

} );