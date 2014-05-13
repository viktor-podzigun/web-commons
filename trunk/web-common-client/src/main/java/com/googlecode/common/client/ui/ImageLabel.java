
package com.googlecode.common.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;


/**
 * Represents label with image.
 */
public final class ImageLabel extends Label {

    private final ImageLabelWrapper wrapper;
    
    
    public ImageLabel() {
        this(null, null);
    }

    public ImageLabel(ImageResource resource) {
        this(resource, null);
    }

    public ImageLabel(ImageResource resource, String text) {
        super(DOM.createSpan());
        
        wrapper = new ImageLabelWrapper(getElement(), resource, text);
    }
    
    @Override
    public String getText() {
        return wrapper.getText();
    }
    
    @Override
    public void setText(String text) {
        wrapper.setText(text);
    }

    public void setImage(ImageResource resource) {
        wrapper.setImage(resource);
    }

}
