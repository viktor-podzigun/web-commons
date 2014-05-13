
package com.googlecode.common.client.ui;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
        // For all browsers - catch onKeyUp
        sinkEvents(Event.ONKEYUP);

        // For IE and Firefox - catch onPaste
        sinkEvents(Event.ONPASTE);
    }

    /**
     * Gets the current placeholder text for the text box.
     * @return the current placeholder text
     */
    public String getPlaceholder() {
        return getElement().getPropertyString("placeholder");
    }

    /**
     * Sets the placeholder text displayed in the text box.
     * 
     * @param placeholder   the placeholder text
     */
    public void setPlaceholder(String text) {
        text = (text != null ? text : "");
        getElement().setPropertyString("placeholder", text);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        switch (event.getTypeInt()) {
        case Event.ONKEYUP:
        case Event.ONPASTE:{
            // Scheduler needed so pasted data shows up in TextBox before we
            // fire event
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    fireEvent(new TextChangeEvent());
                }
            });
            break;
        }
        default:
            // Do nothing
        }
    }

    public HandlerRegistration addTextChangeEventHandler(
            TextChangeEventHandler handler) {
    	
        return addHandler(handler, TextChangeEvent.TYPE);
    }
}
