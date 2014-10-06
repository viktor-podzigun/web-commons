
package com.googlecode.common.client.ui.panel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonsPanel;


/**
 * Wraps Bootstrap modal functionality.
 */
public class Modal extends PopupPanel {

    private final HTMLPanel     modal;
    private final SimplePanel   body;
    private ButtonsPanel        buttonsPanel;


    public Modal(String header, boolean closable) {
        super(closable, true);
        
        String modalId = DOM.createUniqueId();
        modal = createModal(modalId, header, closable);
        
        body = new SimplePanel();
        body.setStyleName("modal-body");
        modal.add(body, modalId);
        
        setWidget(modal);
        setGlassEnabled(true);
    }
    
    private HTMLPanel createModal(String id, String header, boolean closable) {
        HTMLPanel panel = new HTMLPanel("");
        panel.setStyleName("gwt-modal");
        
        if (header != null) {
            HTMLPanel headerPanel = new HTMLPanel("");
            headerPanel.setStyleName("modal-header");
            
            if (closable) {
                Button closeBtn = new Button(new SafeHtmlBuilder()
                        .appendHtmlConstant("&times;").toSafeHtml());
                closeBtn.setStyleName("close");
                closeBtn.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        hide();
                    }
                });
                
                headerPanel.add(closeBtn);
            }
            
            headerPanel.add(new HTMLPanel("h3", header));
            panel.add(headerPanel);
        }
        
        Element el = panel.getElement();
        el.setId(id);
        if (!closable) {
            el.setAttribute("data-backdrop", "static");
            el.setAttribute("data-keyboard", "false");
        }
        
        return panel;
    }
    
    public Widget getBody() {
        return body.getWidget();
    }
    
    public void setBody(Widget widget) {
        body.setWidget(widget);
    }
    
    public ButtonsPanel getButtonsPanel() {
        if (buttonsPanel == null) {
            buttonsPanel = new ButtonsPanel();
            buttonsPanel.setStyleName("modal-footer");
            modal.add(buttonsPanel, modal.getElement().getId());
        }
        
        return buttonsPanel;
    }

    public void setWidth (Double width) {
        modal.getElement().getStyle().setWidth(width, Unit.PX);
        modal.getElement().getStyle().setMarginLeft(-width / 2, Unit.PX);
    }
    
    public void setMaxHeight(Double maxHeight) {
        body.getElement().setAttribute("style",
                "max-height: " + maxHeight + "px; " 
                        + body.getElement().getAttribute("style"));
    }
    
//    public void show() {
//        //RootPanel.get().add(modal);
//        //setVisible0(modal.getElement().getId(), true);
//    }
//
//    public void hide() {
//        setVisible0(modal.getElement().getId(), false);
//        RootPanel.get().remove(modal);
//    }
//
//    private native void setVisible0(String id, boolean visible) /*-{
//        $wnd.$("#" + id).modal(visible ? "show" : "hide");
//    }-*/;
    
}
