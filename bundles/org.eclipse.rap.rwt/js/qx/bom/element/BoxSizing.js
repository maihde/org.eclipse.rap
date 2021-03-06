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
 ******************************************************************************/

/**
 * Contains methods to control and query the element's box-sizing property.
 *
 * Supported values:
 *
 * * "content-box" = W3C model (dimensions are content specific)
 * * "border-box" = Microsoft model (dimensions are box specific incl. border and padding)
 */
qx.Class.define("qx.bom.element.BoxSizing",
{
  /*
  *****************************************************************************
     STATICS
  *****************************************************************************
  */

  statics :
  {
    /** {Map} Internal helper structure to return the valid box-sizing style property names */
    __styleProperties : qx.core.Variant.select("qx.client",
    {
      "mshtml|newmshtml" : null,
      "webkit" : ["boxSizing", "KhtmlBoxSizing", "WebkitBoxSizing"],
      "gecko" : ["MozBoxSizing", "boxSizing"],
      "opera" : ["boxSizing"]
    }),

    /** {Map} Internal data structure for __usesNativeBorderBox() */
    __nativeBorderBox :
    {
      tags :
      {
        button : true,
        select : true
      },

      types :
      {
        search : true,
        button : true,
        submit : true,
        reset : true,
        checkbox : true,
        radio : true
      }
    },


    /**
     * Whether the given elements defaults to the "border-box" Microsoft model in all cases.
     *
     * @param element {Element} DOM element to query
     * @return {Boolean} true when the element uses "border-box" independently from the doctype
     */
    __usesNativeBorderBox : function(element)
    {
      var map = this.__nativeBorderBox;
      return map.tags[element.tagName.toLowerCase()] || map.types[element.type];
    },


    /**
     * Returns the box sizing for the given element.
     *
     * @type static
     * @signature function(element)
     * @param element {Element} The element to query
     * @return {String} Box sizing value of the given element.
     */
    get : qx.core.Variant.select("qx.client",
    {
      "mshtml" : function(element)
      {
        if (qx.bom.Document.isStandardMode(qx.dom.Node.getDocument(element)))
        {
          if (!this.__usesNativeBorderBox(element)) {
            return "content-box";
          }
        }

        return "border-box";
      },

      "default" : function(element)
      {
        var props = this.__styleProperties;
        var value;

        if (props)
        {
          for (var i=0, l=props.length; i<l; i++)
          {
            value = qx.bom.element.Style.get(element, props[i], null, false);
            if (value != null && value !== "") {
              return value;
            }
          }
        }
      }
    })

  }
});
