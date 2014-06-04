
package com.googlecode.common.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;


/**
 * Cell that renders image along with text.
 */
public class ImageTextCell<T> extends AbstractCell<T> {
    
    private final ImageLabelWrapper wrapper = new ImageLabelWrapper(
            DOM.createSpan());
    
    
    public ImageTextCell() {
    }
    
    public ImageTextCell(ImageResource resource) {
        wrapper.setImage(resource);
    }
    
    public void setImage(ImageResource resource) {
        wrapper.setImage(resource);
    }

    @Override
    public void render(Context context, T value, SafeHtmlBuilder sb) {
        wrapper.setText(value.toString());
        
        sb.appendHtmlConstant(wrapper.getElement().getString());
    }

}
