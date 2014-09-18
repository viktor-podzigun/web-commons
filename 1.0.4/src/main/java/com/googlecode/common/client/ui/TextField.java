
package com.googlecode.common.client.ui;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.text.TextChangeEvent;
import com.googlecode.common.client.ui.text.TextChangeEventHandler;


/**
 * Custom text field with support of standard HTML attributes like placeholder.
 * 
 * <p>Got from here:<br>
 * http://stackoverflow.com/questions/3939490/custom-attributes-in-uibinder-widgets
 */
public final class TextField extends TextBox {

    /**
     * Creates an empty text box.
     */
    public TextField() {
        sinkTextChangeEvents(this);
    }

    /**
     * Gets the current placeholder text for the text box.
     * @return the current placeholder text
     */
    public String getPlaceholder() {
        return getPlaceholder(this);
    }

    /**
     * Sets the placeholder text displayed in the text box.
     * 
     * @param placeholder   the placeholder text
     */
    public void setPlaceholder(String text) {
        setPlaceholder(this, text);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        onBrowserEvent(this, event);
    }

    public HandlerRegistration addTextChangeEventHandler(
            TextChangeEventHandler handler) {
    	
        return addHandler(handler, TextChangeEvent.TYPE);
    }
    
    static void sinkTextChangeEvents(Widget textWidget) {
        // For all browsers - catch onKeyUp
        textWidget.sinkEvents(Event.ONKEYUP);

        // For IE and Firefox - catch onPaste
        textWidget.sinkEvents(Event.ONPASTE);
    }

    static void onBrowserEvent(final Widget textWidget, Event event) {
        switch (event.getTypeInt()) {
        case Event.ONKEYUP:
        case Event.ONPASTE:{
            // Scheduler needed so pasted data shows up in text field
            // before we fire event
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    textWidget.fireEvent(new TextChangeEvent());
                }
            });
            break;
        }
        default:
            // Do nothing
        }
    }

    static String getPlaceholder(Widget textWidget) {
        return textWidget.getElement().getPropertyString("placeholder");
    }

    static void setPlaceholder(Widget textWidget, String text) {
        text = (text != null ? text : "");
        textWidget.getElement().setPropertyString("placeholder", text);
    }

}
