
package com.googlecode.common.showcase.client;

import com.google.gwt.user.client.ui.Composite;


/**
 * Contains common functionality for documents/examples panels.
 */
public abstract class AbstractPanelView extends Composite {

    
    @Override
    protected void onLoad() {
        super.onLoad();
        prettyPrint();
    }
    
    /**
     * Pretty prints code examples, using the 
     * <a href="http://code.google.com/p/google-code-prettify/">google-code-prettify</a> 
     * JavaScript library.
     */
    private static native void prettyPrint() /*-{
        $wnd.prettyPrint();
    }-*/;

}
