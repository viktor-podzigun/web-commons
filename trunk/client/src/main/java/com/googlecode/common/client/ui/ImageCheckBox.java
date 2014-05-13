
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;


/**
 * CheckBox that can have additional image element along with the text.
 * 
 * <p>Supports indeterminate state, got from here:<br>
 * http://css-tricks.com/indeterminate-checkboxes/
 */
public final class ImageCheckBox extends CheckBox {

    private final ImageLabelWrapper wrapper;
    
    private TriState                state = TriState.DESELECTED;
    private HandlerRegistration     readOnlyClickReg;
    
    
    public ImageCheckBox() {
        this(null, null);
    }
    
    public ImageCheckBox(String text) {
        this(null, text);
    }
    
    public ImageCheckBox(ImageResource resource, String text) {
        // align check element
        Element check = DOM.getFirstChild(getElement());
        check.setAttribute("style", check.getAttribute("style") 
                + "; vertical-align:middle;");
        
        // move check element to label
        Element label = DOM.getNextSibling(check);
        check.removeFromParent();
        label.appendChild(check);
        
        wrapper = new ImageLabelWrapper(label, resource, text);
    }
    
    public TriState getState() {
        return state;
    }
    
    public void setState(TriState state) {
        if (state == null) {
            throw new NullPointerException("state");
        }
        if (this.state == state) {
            return;
        }
        
        this.state = state;
        
        switch (state) {
        case DESELECTED:
            setValue(false, false);
            break;
        
        case SELECTED:
            setValue(true, false);
            break;
        
        case INDETERMINATE:
            setValue(false, false);
            setIndeterminate(true);
            return;
        
        default:
            throw new IllegalStateException("Unknown state: " + state);
        }
    
        setIndeterminate(false);
    }
    
    private void setIndeterminate(boolean indeterminate) {
        // first, set to deselected
        if (indeterminate) {
            setValue(false, false);
        }
        
        // apply indeterminate property
        Element label = DOM.getFirstChild(getElement());
        DOM.getFirstChild(label).setPropertyBoolean(
                "indeterminate", indeterminate);
    }

    @Override
    public String getText() {
        return wrapper.getText();
    }
    
    @Override
    public void setText(String text) {
        wrapper.setText(text);
    }

    @Override
    public void setEnabled(boolean enabled) {
        wrapper.setEnabled(enabled);
        
        super.setEnabled(enabled);
    }

    public void setImage(ImageResource resource) {
        wrapper.setImage(resource);
    }

    public void setDisabledImage(ImageResource resource) {
        wrapper.setDisabledImage(isEnabled(), resource);
    }

    public boolean isReadOnly() {
        return (readOnlyClickReg != null);
    }
    
    public void setReadOnly(boolean readOnly) {
        if (isReadOnly() == readOnly) {
            return;
        }
        
        if (readOnly) {
            readOnlyClickReg = addClickHandler(ReadOnlyHandler.INSTANCE);
        } else {
            readOnlyClickReg.removeHandler();
            readOnlyClickReg = null;
        }
    }
    
    
    private static class ReadOnlyHandler implements ClickHandler {
        
        private static final ReadOnlyHandler INSTANCE = new ReadOnlyHandler();
        
        private ReadOnlyHandler() {
        }
        
        @Override
        public void onClick(ClickEvent event) {
            event.stopPropagation();
            event.preventDefault();
        }
    }

}
