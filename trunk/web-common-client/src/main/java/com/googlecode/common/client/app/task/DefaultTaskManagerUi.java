
package com.googlecode.common.client.app.task;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.googlecode.common.client.task.TaskManagerUi;
import com.googlecode.common.client.ui.CommonImages;
import com.googlecode.common.client.ui.panel.ErrorPanel;


/**
 * Default implementation of {@link TaskManagerUi} interface.
 */
public final class DefaultTaskManagerUi implements TaskManagerUi {

    private PopupPanel              loadingPopup;
    private DecoratedPopupPanel     statusPopup;
    
    
    @Override
    public void showError(String error) {
        ErrorPanel.show(error);
    }

    @Override
    public void showError(String error, Throwable exception) {
        ErrorPanel.showError(error, exception);
    }

    @Override
    public void showError(String error, String details) {
        ErrorPanel.showDetailed(error, details);
    }

    @Override
    public void mask(String text) {
        showStatus(text);
        
        if (loadingPopup == null) {
            // create a modal pop-up
            loadingPopup = new PopupPanel(false, true);
            //popup.add(new Label("Please wait..."));
            loadingPopup.add(new Image(CommonImages.INSTANCE.loading()));
        
            // show disabled area in gray color
            //popup.setGlassEnabled(true);
        }
        
        // center the pop-up and make it visible
        loadingPopup.center();
        
        // show wait cursor
        RootPanel.get().getElement().getStyle().setCursor(Cursor.WAIT);
    }
    
    @Override
    public void unmask(String text) {
        // restore default cursor
        RootPanel.get().getElement().getStyle().setCursor(Cursor.DEFAULT);
        
        if (loadingPopup != null) {
            loadingPopup.hide();
        }
        
        showStatus(text);
    }

    private void showStatus(String text) {
        if (statusPopup == null) {
            // create auto-hide pop-up panel
            statusPopup = new DecoratedPopupPanel(true);
            statusPopup.add(new Label());
        }
        
        // update status text
        ((Label)statusPopup.getWidget()).setText(text);
        
        if (!statusPopup.isShowing()) {
            statusPopup.setPopupPositionAndShow(new PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {
                    // position at the bottom-left corner
                    statusPopup.setPopupPosition(0, 
                            Window.getClientHeight() - offsetHeight);
                }
            });
        }
    }
    
}
