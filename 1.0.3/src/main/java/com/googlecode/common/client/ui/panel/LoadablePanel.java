
package com.googlecode.common.client.ui.panel;

import com.google.gwt.user.client.ui.Composite;


/**
 * Base panel for loadable content.
 */
public abstract class LoadablePanel extends Composite {

    private boolean isNeedLoad = true;
    
    
    protected LoadablePanel() {
    }
    
    public boolean isNeedLoad() {
        return isNeedLoad;
    }
    
    public void setNeedLoad(boolean isNeedLoad) {
        this.isNeedLoad = isNeedLoad;
    }

    public void onActivated() {
        if (isNeedLoad) {
            isNeedLoad = false;
            
            onLoadData();
        }
    }

    public void onDeactivated() {
        isNeedLoad = true;
    }
    
    public abstract void onLoadData();

}
