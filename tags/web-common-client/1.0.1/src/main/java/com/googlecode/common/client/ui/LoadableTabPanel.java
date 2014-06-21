
package com.googlecode.common.client.ui;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.panel.LoadablePanel;


/**
 * Useful {@link TabPanel} extension that adds load action.
 */
public class LoadableTabPanel extends TabPanel {
    
    private Widget      currTab;
    

    public LoadableTabPanel() {
        this(Direction.TOP);
    }
    
    public LoadableTabPanel(Direction direction) {
        super(direction);
        
        addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                onStateChanged(event.getSelectedItem());
            }
        });
    }

    protected void onStateChanged(int index) {
        if (index == -1) {
            return;
        }
        
        Widget tab = getWidget(index);
        if (currTab == tab) {
            return;
        }
        
        if (currTab instanceof LoadablePanel) {
            ((LoadablePanel)currTab).onDeactivated();
        }
        
        if (isAttached() && tab instanceof LoadablePanel) {
            ((LoadablePanel)tab).onActivated();
        }
        
        currTab = tab;
    }
    
    public void reloadActiveTab() {
        if (currTab instanceof LoadablePanel) {
            ((LoadablePanel)currTab).onLoadData();
        }
    }
}
