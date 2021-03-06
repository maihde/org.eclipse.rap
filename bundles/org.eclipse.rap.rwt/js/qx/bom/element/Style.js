/*******************************************************************************
 *  Copyright: 2004, 2010 1&1 Internet AG, Germany, http://www.1und1.de,
 *                        and EclipseSource
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    1&1 Internet AG and others - original API and implementation
 *    EclipseSource - adaptation for the Eclipse Rich Ajax Platform
 *
 *   This class contains code based on the following work:
 *
 *   * Prototype JS
 *     http://www.prototypejs.org/
 *     Version 1.5
 *
 *     Copyright:
 *       (c) 2006-2007, Prototype Core Team
 *
 *     License:
 *       MIT: http://www.opensource.org/licenses/mit-license.php
 *
 *     Authors:
 *       * Prototype Core Team
 *
 ******************************************************************************/

/**
 * Style querying and modification of HTML elements.
 *
 * Automatically normalizes cross-browser differences. Optimized for
 * performance.
 */
qx.Class.define("qx.bom.element.Style",
{
  /*
  *****************************************************************************
     STATICS
  *****************************************************************************
  */

  statics :
  {
    /** Internal map of style property convertions */
    __hints :
    {
      // Style property name correction
      names :
      {
        "float" : org.eclipse.rwt.Client.isMshtml() ? "styleFloat" : "cssFloat",
        "boxSizing" : org.eclipse.rwt.Client.isGecko() ? "mozBoxSizing" : "boxSizing"
      },

      // Mshtml has propertiery pixel* properties for locations and dimensions
      // which return the pixel value. Used by getComputed() in mshtml variant.
      mshtmlPixel :
      {
        width : "pixelWidth",
        height : "pixelHeight",
        left : "pixelLeft",
        right : "pixelRight",
        top : "pixelTop",
        bottom : "pixelBottom"
      }

    },


    /*
    ---------------------------------------------------------------------------
      STYLE ATTRIBUTE SUPPORT
    ---------------------------------------------------------------------------
    */

    /** {Integer} Computed value of a style property. Compared to the cascaded style,
     * this one also interprets the values e.g. translates <code>em</code> units to
     * <code>px</code>.
     */
    COMPUTED_MODE : 1,


    /** {Integer} Cascaded value of a style property. */
    CASCADED_MODE : 2,


    /** {Integer} Local value of a style property. Ignores inheritance cascade. Does not interpret values. */
    LOCAL_MODE : 3,

    /**
     * Gets the value of a style property.
     *
     * *Computed*
     *
     * Returns the computed value of a style property. Compared to the cascaded style,
     * this one also interprets the values e.g. translates <code>em</code> units to
     * <code>px</code>.
     *
     * *Cascaded*
     *
     * Returns the cascaded value of a style property.
     *
     * *Local*
     *
     * Ignores inheritance cascade. Does not interpret values.
     *
     * @type static
     * @signature function(element, name, mode, smart)
     * @param element {Element} The DOM element to modify
     * @param name {String} Name of the style attribute (js variant e.g. marginTop, wordSpacing)
     * @param mode {Number} Choose one of the modes {@link #COMPUTED_MODE}, {@link #CASCADED_MODE},
     *   {@link #LOCAL_MODE}. The computed mode is the default one.
     * @param smart {Boolean?true} Whether the implementation should automatically use
     *    special implementations for some properties
     * @return {var} The value of the property
     */
    get : qx.core.Variant.select("qx.client",
    {
      "mshtml" : function(element, name, mode, smart)
      {
        var hints = this.__hints;

        // normalize name
        name = hints.names[name] || name;

        // switch to right mode
        switch(mode)
        {
          case this.LOCAL_MODE:
            return element.style[name] || "";

          case this.CASCADED_MODE:
            return element.currentStyle[name];

          default:
            // Read cascaded style
            var currentStyle = element.currentStyle[name];

            // Pixel values are always OK
            if (/^-?[\.\d]+(px)?$/i.test(currentStyle)) {
              return currentStyle;
            }

            // Try to convert non-pixel values
            var pixel = hints.mshtmlPixel[name];
            if (pixel)
            {
              // Backup local and runtime style
              var localStyle = element.style[name];

              // Overwrite local value with cascaded value
              // This is needed to have the pixel value setupped
              element.style[name] = currentStyle || 0;

              // Read pixel value and add "px"
              var value = element.style[pixel] + "px";

              // Recover old local value
              element.style[name] = localStyle;

              // Return value
              return value;
            }

            // Non-Pixel values may be problematic
            if (/^-?[\.\d]+(em|pt|%)?$/i.test(currentStyle)) {
              throw new Error("Untranslated computed property value: " + name + ". Only pixel values work well across different clients.");
            }

            // Just the current style
            return currentStyle;
        }
      },

      "default" : function(element, name, mode, smart)
      {
        var hints = this.__hints;

        // normalize name
        name = hints.names[name] || name;

        // switch to right mode
        switch(mode)
        {
          case this.LOCAL_MODE:
            return element.style[name];

          case this.CASCADED_MODE:
            // Currently only supported by Opera and Internet Explorer
            if (element.currentStyle) {
              return element.currentStyle[name];
            }

            throw new Error("Cascaded styles are not supported in this browser!");

          // Support for the DOM2 getComputedStyle method
          //
          // Safari >= 3 & Gecko > 1.4 expose all properties to the returned
          // CSSStyleDeclaration object. In older browsers the function
          // "getPropertyValue" is needed to access the values.
          //
          // On a computed style object all properties are read-only which is
          // identical to the behavior of MSHTML's "currentStyle".
          default:
            // Opera, Mozilla and Safari 3+ also have a global getComputedStyle which is identical
            // to the one found under document.defaultView.

            // The problem with this is however that this does not work correctly
            // when working with frames and access an element of another frame.
            // Then we must use the <code>getComputedStyle</code> of the document
            // where the element is defined.
            var doc = qx.dom.Node.getDocument(element);
            var computed = doc.defaultView.getComputedStyle(element, null);

            // All relevant browsers expose the configured style properties to
            // the CSSStyleDeclaration objects
            return computed ? computed[name] : null;
        }
      }
    })
  }
});
