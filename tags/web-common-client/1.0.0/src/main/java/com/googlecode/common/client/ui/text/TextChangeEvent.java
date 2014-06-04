
package com.googlecode.common.client.ui.text;

import com.google.gwt.event.shared.GwtEvent;


/**
 * Text change event for such events as KeyUP or PASTE.
 */
public class TextChangeEvent extends GwtEvent<TextChangeEventHandler> {

    public static final Type<TextChangeEventHandler> TYPE = 
        new Type<TextChangeEventHandler>();

    
    @Override
    public Type<TextChangeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TextChangeEventHandler handler) {
        handler.onTextChange(this);
    }
}
