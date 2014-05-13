
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;


/**
 * Button with dropdown menu.
 */
public final class DropdownButton extends AbstractButton implements 
        HasAllFocusHandlers {

    private final Button    toggle;
    
    
    public DropdownButton(String text, PopupMenu menu) {
        this(text, menu, DOM.createSpan());
    }
    
    private DropdownButton(String text, PopupMenu menu, Element el) {
        super(new ImageLabelWrapper(el, null, text));
        
        toggle = new Button();
        toggle.setStyleName("btn dropdown-toggle");
        toggle.getElement().appendChild(el);
        toggle.getElement().setAttribute("data-toggle", "dropdown");
        
        Element span = DOM.createSpan();
        span.setClassName("caret");
        toggle.getElement().appendChild(span);
        
        FlowPanel widget = new FlowPanel();
        widget.setStyleName("btn-group");
        widget.add(toggle);
        widget.add(menu);
        
        initWidget(widget);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        toggle.setEnabled(enabled);
    }
    
    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return toggle.addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return toggle.addBlurHandler(handler);
    }

    @Override
    public int getTabIndex() {
        return toggle.getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        toggle.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        toggle.setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        toggle.setTabIndex(index);
    }

}
