
package com.googlecode.common.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;


/**
 * Anchor button implementation. Useful for exporting data.
 */
public class AnchorButton extends AbstractButton implements 
        HasClickHandlers, HasAllFocusHandlers {

    
    public AnchorButton() {
        this(null, null);
    }

    public AnchorButton(ImageResource resource) {
        this(resource, null);
    }

    public AnchorButton(String text) {
        this(null, text);
    }
    
    public AnchorButton(ImageResource resource, String text) {
        this(resource, text, null);
    }
    
    public AnchorButton(ImageResource resource, String text, 
            ClickHandler handler) {
        
        this(resource, text, new Anchor(), handler);
    }
    
    AnchorButton(ImageResource resource, String text, Anchor btn, 
            final ClickHandler handler) {
        
        super(new ImageLabelWrapper(btn.getElement(), resource, text));
        
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!isEnabled()) {
                    event.preventDefault();
                    return;
                }
                
                if (handler != null) {
                    handler.onClick(event);
                    
                    event.preventDefault();
                }
            }
        });
        
        btn.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!isEnabled()) {
                    event.preventDefault();
                    return;
                }
                
                if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER 
                        || event.getNativeKeyCode() == 32/* space */)){
                    
                    _click(AnchorButton.this.getElement());
                    
                    event.preventDefault();
                } 
            }
            
            public native void _click(Element element)/*-{
                element.click();
            }-*/;
        });
        
        btn.setStyleName("btn");
        btn.setTabIndex(-1);
        initWidget(btn);
    }
    
    Anchor getAnchor() {
        return (Anchor)getWidget();
    }
    
    public void setHref(String href) {
        getAnchor().setHref(href);
    }
    
    public void setPrimary(boolean primary) {
        if (primary) {
            getAnchor().addStyleName("btn-primary");
        } else {
            getAnchor().removeStyleName("btn-primary");
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        if (!enabled) {
            getAnchor().addStyleName("disabled");
        } else {
            getAnchor().removeStyleName("disabled");
        }
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return getAnchor().addClickHandler(handler);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return getAnchor().addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return getAnchor().addBlurHandler(handler);
    }

    @Override
    public int getTabIndex() {
        return getAnchor().getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        getAnchor().setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        getAnchor().setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        getAnchor().setTabIndex(index);
    }

}
