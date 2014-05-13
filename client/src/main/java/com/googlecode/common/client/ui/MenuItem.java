
package com.googlecode.common.client.ui;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;


public final class MenuItem extends AbstractButton {

    protected final HTMLPanel   itemPanel;
    private final Anchor        anchor;
    
    
    public MenuItem(String text) {
        this(null, text, null, null, false);
    }
    
    public MenuItem(String text, ScheduledCommand cmd) {
        this(null, text, cmd);
    }
    
    public MenuItem(String text, PopupMenu subMenu) {
        this(null, text, subMenu);
    }
    
    public MenuItem(String text, PopupMenu menu, boolean rootMenu) {
        this(null, text, null, menu, rootMenu);
    }
    
    public MenuItem(ImageResource resource, String text, ScheduledCommand cmd) {
        this(resource, text, cmd, null, false);
    }
    
    public MenuItem(ImageResource resource, String text, PopupMenu subMenu) {
        this(resource, text, null, subMenu, false);
    }
    
    private MenuItem(ImageResource resource, String text, ScheduledCommand cmd, 
            PopupMenu subMenu, boolean rootMenu) {
        
        this(resource, text, cmd, subMenu, rootMenu, new Anchor());
    }
        
    private MenuItem(ImageResource resource, String text, 
            final ScheduledCommand cmd, PopupMenu menu, boolean rootMenu, 
            Anchor anchor) {
        
        super(new ImageLabelWrapper(anchor.getElement(), resource, text));
        
        this.anchor = anchor;
        
        itemPanel = new HTMLPanel("li", "");
        itemPanel.add(anchor);
        
        initWidget(itemPanel);
        
        anchor.setHref("#");
        
        if (cmd != null) {
            anchor.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    event.preventDefault();
                    cmd.execute();
                }
            });
        } else if (menu != null) {
            if (rootMenu) {
                itemPanel.setStyleName("dropdown");
                anchor.setStyleName("dropdown-toggle");
                anchor.getElement().setAttribute("data-toggle", "dropdown");
                
                Element b = DOM.createElement("b");
                b.addClassName("caret");
                anchor.getElement().appendChild(b);
            
            } else {
                itemPanel.setStyleName("dropdown-submenu");
            }
            
            itemPanel.add(menu);
        }
    }
    
    public void setTargetId(String targetId) {
        wrapper.getElement().setAttribute("data-target", "#" + targetId);
    }
    
    public void setTargetUrl(String targetUrl) {
        anchor.setHref(targetUrl);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        if (enabled) {
            itemPanel.removeStyleName("disabled");
        } else {
            itemPanel.addStyleName("disabled");
        }
    }
    
    @Override
    public int getTabIndex() {
        return anchor.getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        anchor.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        anchor.setFocus(focused);
    }

    @Override
    public void setTabIndex(int index) {
        anchor.setTabIndex(index);
    }

}
