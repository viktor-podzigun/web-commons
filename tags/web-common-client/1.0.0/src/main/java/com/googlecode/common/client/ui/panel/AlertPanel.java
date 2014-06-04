
package com.googlecode.common.client.ui.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Panel for displaying warning messages.
 */
public final class AlertPanel extends Composite {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, AlertPanel> {
    }

    @UiField SimplePanel    content;
    
    
    public AlertPanel() {
        this(null);
    }
    
    public AlertPanel(Widget content) {
        initWidget(binder.createAndBindUi(this));
        
        if (content != null) {
            setContent(content);
        }
    }
    
    @UiHandler("closeBtn")
    void onCloseBtnClick(ClickEvent event) {
        removeFromParent();
    }
    
    public void setContent(Widget content) {
        this.content.setWidget(content);
    }

}
