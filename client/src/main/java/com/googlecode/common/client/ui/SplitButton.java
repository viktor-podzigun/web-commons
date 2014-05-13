
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;


/**
 * Button with dropdown menu and default command.
 */
public final class SplitButton extends AbstractButton implements 
        HasClickHandlers, HasAllFocusHandlers {

    private final Button    btn;
    
    
    public SplitButton(String text, PopupMenu menu) {
        this(text, menu, null, DOM.createSpan());
    }
    
    public SplitButton(String text, PopupMenu menu, ClickHandler handler) {
        this(text, menu, handler, DOM.createSpan());
    }
    
    private SplitButton(String text, PopupMenu menu, 
            ClickHandler handler, Element el) {
        
        super(new ImageLabelWrapper(el, null, text));
        
        btn = new Button();
        btn.setStyleName("btn");
        btn.getElement().appendChild(el);
        
        if (handler != null) {
            btn.addClickHandler(handler);
        }
        
        Button toggle = new Button();
        toggle.setStyleName("btn dropdown-toggle");
        toggle.getElement().setAttribute("data-toggle", "dropdown");
        
        Element span = DOM.createSpan();
        span.setClassName("caret");
        toggle.getElement().appendChild(span);
        
        FlowPanel widget = new FlowPanel();
        widget.setStyleName("btn-group");
        widget.add(btn);
        widget.add(toggle);
        widget.add(menu);
        
        initWidget(widget);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        FlowPanel panel = (FlowPanel)getWidget();
        for (Widget w : panel) {
            if (w instanceof FocusWidget) {
                ((FocusWidget)w).setEnabled(enabled);
            }
        }
    }
    
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return btn.addClickHandler(handler);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return btn.addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return btn.addBlurHandler(handler);
    }

    @Override
    public int getTabIndex() {
        return btn.getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        btn.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        btn.setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        btn.setTabIndex(index);
    }

}
