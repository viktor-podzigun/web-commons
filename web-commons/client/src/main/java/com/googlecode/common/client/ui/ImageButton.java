
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;


/**
 * GWT's button doesn't allow to have image AND text at the same time, 
 * so that's why this class is here.
 * 
 * <p>Got from here:<br>
 * http://juristr.com/blog/2010/03/gwt-button-with-image-and-text/<br>
 * http://stackoverflow.com/questions/1853042/creating-custom-button-in-gwt
 */
public final class ImageButton extends AbstractButton implements 
        HasClickHandlers, HasAllFocusHandlers {

    
    public ImageButton() {
        this(null, null);
    }

    public ImageButton(ImageResource resource) {
        this(resource, null);
    }

    public ImageButton(String text) {
        this(null, text);
    }
    
    public ImageButton(ImageResource resource, String text) {
        this(resource, text, null);
    }
    
    public ImageButton(ImageResource resource, String text, 
            ClickHandler handler) {
        
        this(resource, text, new Button(), handler);
    }
    
    private ImageButton(ImageResource resource, String text, Button btn, 
            ClickHandler handler) {
        
        super(new ImageLabelWrapper(btn.getElement(), resource, text));
        
        if (handler != null) {
            btn.addClickHandler(handler);
        }
        
        btn.setStyleName("btn");
        initWidget(btn);
    }
    
    private Button getButton() {
        return (Button)getWidget();
    }
    
    public void setPrimary(boolean primary) {
        if (primary) {
            getButton().addStyleName("btn-primary");
        } else {
            getButton().removeStyleName("btn-primary");
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        getButton().setEnabled(enabled);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return getButton().addClickHandler(handler);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return getButton().addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return getButton().addBlurHandler(handler);
    }

    @Override
    public int getTabIndex() {
        return getButton().getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        getButton().setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        getButton().setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        getButton().setTabIndex(index);
    }

}
