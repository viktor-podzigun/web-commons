
package com.googlecode.common.client.ui.panel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;


/**
 * Contains base dialog functionality.
 */
public abstract class BaseDialog {

    static final String CMD_OK     = ButtonType.OK.getCommand();
    static final String CMD_CANCEL = ButtonType.CANCEL.getCommand();
    
    private final Modal modal;
    
    
    protected BaseDialog(String title) {
        this.modal = new Modal(title, false);
    }
    
    protected ButtonsPanel getButtonsPanel() {
        return modal.getButtonsPanel();
    }
        
    public void show() {
        modal.show();
    }

    public void hide() {
        modal.hide();
    }

    public Widget getContent() {
        return modal.getBody();
    }

    public void setContent(Widget body) {
        modal.setBody(body);
    }
    
    public void setWidth (Double width) {
        modal.setWidth(width);
    }

    public void setMaxHeight(Double maxHeight) {
        modal.setMaxHeight(maxHeight);
    }
    
    public static Double getMaxAvailableHeight() {
        // Hardcoded params of modal dialog
        Double topPercent = 10d;
        Double headerHeight = 49d;
        Double footerHeight = 61d;
        Double padding = 15d;
        
        return Window.getClientHeight() * (100 - topPercent) / 100d 
            - headerHeight
            - footerHeight
            - padding * 2;
    }    
}
