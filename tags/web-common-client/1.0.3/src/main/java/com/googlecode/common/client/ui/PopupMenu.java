
package com.googlecode.common.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;


/**
 * Represents Bootstrap dropdown menu.
 */
public final class PopupMenu extends Composite {

    private final HTMLPanel menuPanel;
    
    
    public PopupMenu() {
        menuPanel = new HTMLPanel("ul", "");
        initWidget(menuPanel);
        
        menuPanel.setStyleName("dropdown-menu");
    }
    
    public void addMenuItem(MenuItem item) {
        menuPanel.add(item);
    }
    
    public void addSeparator() {
        HTMLPanel li = new HTMLPanel("li", "");
        li.setStyleName("divider");
        menuPanel.add(li);
    }
    
    /**
     * Removes all child widgets.
     */
    public void clear() {
        menuPanel.clear();
    }

}
