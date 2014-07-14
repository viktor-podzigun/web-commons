
package com.googlecode.common.client.ui;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.panel.ErrorPanel;
import com.googlecode.common.client.util.DateHelpers;


/**
 * Represents generic filter panel.
 */
public abstract class FilterPanel extends Composite {

    protected final KeyDownHandler enterKeyHandler = new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                onSearch();
            }
        }
    };
    
    
    protected FilterPanel() {
    }
    
    protected void initPanel(Panel inlinePanel, Widget filterPanel) {
        inlinePanel.removeFromParent();
        inlinePanel.add(new FilterButtons(this, filterPanel));
    
        FlowPanel panel = new FlowPanel();
        panel.add(inlinePanel);
        panel.add(filterPanel);
        
        initWidget(panel);
    }
    
    protected abstract void onSearch();

    protected static Date tryParseDateUtc(String strToParse, String name) {
        try {
            return DateHelpers.utcParseDate(strToParse);
        
        } catch(IllegalArgumentException x) {
            ErrorPanel.show(name + " is invalid");
            return null;
        }
    }
    
    protected static Long tryParseLong(String strToParse, String name) {
        try {
            return Long.valueOf(Long.parseLong(strToParse));
        
        } catch(IllegalArgumentException e) {
            ErrorPanel.show(name + " is invalid");
        }
        
        return null;
    }
    
    protected static Integer tryParseInt(String strToParse, String name) {
        try {
            return Integer.valueOf(Integer.parseInt(strToParse));
        
        } catch(IllegalArgumentException e) {
            ErrorPanel.show(name + " is invalid");
        }
        
        return null;
    }
    
}


class FilterButtons extends Composite {
    
    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, FilterButtons> {
    }
    
    @UiField Button             btnFilter;
    @UiField Button             btnSearch;
    
    private final FilterPanel   filter;
    private final Widget        panel;
    
    
    public FilterButtons(FilterPanel filter, Widget panel) {
        this.filter = filter;
        this.panel  = panel;
        
        initWidget(binder.createAndBindUi(this));
        
        if (DOM.getInnerHTML(panel.getElement()).isEmpty()) {
            btnFilter.setVisible(false);
        }
    }

    @UiHandler("btnSearch")
    void onSearchBtn(ClickEvent event) {
        filter.onSearch();
    }
    
    @UiHandler("btnFilter")
    void onFilterBtn(ClickEvent event) {
        if (panel.isVisible()) {
            panel.setVisible(false);
            btnFilter.setText("Filter >>");
        } else {
            panel.setVisible(true);
            btnFilter.setText("Filter <<");
        }
    }
    
}

