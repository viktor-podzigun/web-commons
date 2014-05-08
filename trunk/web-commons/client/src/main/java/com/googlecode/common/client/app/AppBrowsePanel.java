
package com.googlecode.common.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;


/**
 * Browse panel widget.
 */
public final class AppBrowsePanel extends Composite {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, AppBrowsePanel> {
    }

    @UiField(provided=true) ButtonsPanel    buttonsPanel;
    @UiField(provided=true) BrowseTreePanel browsePanel;
    @UiField SimplePanel                    contentPanel;
    
    
    public AppBrowsePanel() {
        buttonsPanel = new ButtonsPanel(ButtonsPanel.LEFT, 
                ButtonType.REFRESH, ButtonType.ADD, ButtonType.REMOVE, 
                ButtonType.EDIT);
        buttonsPanel.setGroup(true);
        buttonsPanel.setShowText(false);
        browsePanel = new BrowseTreePanel(buttonsPanel);
        
        initWidget(binder.createAndBindUi(this));
        
        browsePanel.setContentPanel(contentPanel);
    }
    
    public BrowseTreePanel getTreePanel() {
        return browsePanel;
    }
    
}
