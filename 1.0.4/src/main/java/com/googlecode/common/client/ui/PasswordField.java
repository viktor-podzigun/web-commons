
package com.googlecode.common.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.googlecode.common.client.ui.text.TextChangeEvent;
import com.googlecode.common.client.ui.text.TextChangeEventHandler;


/**
 * Custom password field with support of standard HTML attributes like 
 * placeholder.
 * 
 * <p>Got from here:<br>
 * http://stackoverflow.com/questions/3939490/custom-attributes-in-uibinder-widgets
 */
public final class PasswordField extends PasswordTextBox {

    /**
     * Creates an empty text box.
     */
    public PasswordField() {
        TextField.sinkTextChangeEvents(this);
    }

    /**
     * Gets the current placeholder text for the text box.
     * @return the current placeholder text
     */
    public String getPlaceholder() {
        return TextField.getPlaceholder(this);
    }

    /**
     * Sets the placeholder text displayed in the text box.
     * 
     * @param placeholder   the placeholder text
     */
    public void setPlaceholder(String text) {
        TextField.setPlaceholder(this, text);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        TextField.onBrowserEvent(this, event);
    }

    public HandlerRegistration addTextChangeEventHandler(
            TextChangeEventHandler handler) {
    	
        return addHandler(handler, TextChangeEvent.TYPE);
    }

}
