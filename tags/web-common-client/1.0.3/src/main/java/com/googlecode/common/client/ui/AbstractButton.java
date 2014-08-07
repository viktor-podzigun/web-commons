
package com.googlecode.common.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;


/**
 * Contains common functionality for all buttons.
 */
public abstract class AbstractButton extends Composite implements HasEnabled, 
        Focusable {

    protected final ImageLabelWrapper wrapper;
    
    private String      command;
    private boolean     enabled = true;
    
    
    protected AbstractButton(ImageLabelWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public String getCommand() {
        return command;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }

    public String getText() {
        return wrapper.getText();
    }
    
    public void setText(String text) {
        wrapper.setText(text);
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        wrapper.setEnabled(enabled);
    }

    public void setImage(ImageResource resource) {
        wrapper.setImage(resource);
    }

    public void setDisabledImage(ImageResource resource) {
        wrapper.setDisabledImage(isEnabled(), resource);
    }

    public void setButtonInfo(ButtonType type) {
        setButtonInfo(type.getCommand(), type.getImage(), 
                type.getDisabledImage());
    }
    
    public void setButtonInfo(String cmd, ImageResource image, 
            ImageResource disabledImage) {
        
        setCommand(cmd);
        setImage(image);
        setDisabledImage(disabledImage);
    }

    public void setGlyphIcon(String iconName) {
        wrapper.setGlyphIcon(iconName);
    }
    
}
