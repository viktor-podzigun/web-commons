
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasValue;


/**
 * Button with state which allows the user to toggle between 
 * <code>up</code> and <code>down</code> states.
 */
public final class StateButton extends AnchorButton implements 
        HasValue<Boolean>, HasKeyDownHandlers, HasKeyUpHandlers, 
        HasKeyPressHandlers {

    
    public StateButton() {
        this(null, null);
    }

    public StateButton(ImageResource resource) {
        this(resource, null);
    }

    public StateButton(String text) {
        this(null, text);
    }
    
    public StateButton(ImageResource resource, String text) {
        this(resource, text, null);
    }
    
    public StateButton(ImageResource resource, String text, 
            ClickHandler handler) {
        
        this(resource, text, new Anchor(), handler);
    }
    
    private StateButton(ImageResource resource, String text, Anchor btn, 
            final ClickHandler handler) {
        
        super(resource, text, btn, null);
    
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!isEnabled()) {
                    return;
                }
                
                toggleDown();
            
                if (handler != null) {
                    handler.onClick(event);
                }
                
                ValueChangeEvent.fire(StateButton.this, isDown());
            }
        });
    }
    
    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<Boolean> handler) {
        
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Determines whether this button is currently down.
     * 
     * @return  <code>true</code> if the button is pressed, false otherwise. 
     *          Will not return null
     */
    @Override
    public Boolean getValue() {
        return isDown();
    }

    /**
     * Sets whether this button is down.
     * 
     * @param value         true to press the button, false otherwise; 
     *                      null value implies false
     */
    @Override
    public void setValue(Boolean value) {
        setValue(value, false);
    }

    /**
     * Sets whether this button is down, firing {@link ValueChangeEvent} if
     * appropriate.
     * 
     * @param value         true to press the button, false otherwise; 
     *                      null value implies false
     * @param fireEvents    If true, and value has changed, fire a
     *                      {@link ValueChangeEvent}
     */
    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        if (value == null) {
            value = Boolean.FALSE;
        }
        
        boolean oldValue = isDown();
        setDown(value);
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }

    /**
     * Toggle the up/down attribute.
     */
    private void toggleDown() {
        if (isDown()) {
            getAnchor().removeStyleName("active");
        } else {
            getAnchor().addStyleName("active");
        }
    }

    public boolean isDown() {
        return getAnchor().getStyleName().contains("active");
    }

    /**
     * {@inheritDoc} Does not fire {@link ValueChangeEvent}. (If you want the
     * event to fire, use {@link #setValue(Boolean, boolean)})
     */
    public void setDown(boolean down) {
        if (down != isDown()) {
            toggleDown();
        }
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return getAnchor().addKeyDownHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return getAnchor().addKeyPressHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return getAnchor().addKeyUpHandler(handler);
    }

}
