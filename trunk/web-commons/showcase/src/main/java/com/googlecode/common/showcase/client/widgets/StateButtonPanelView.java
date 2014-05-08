
package com.googlecode.common.showcase.client.widgets;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.CommonImages;
import com.googlecode.common.client.ui.StateButton;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.showcase.client.AbstractPanelView;


public final class StateButtonPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, StateButtonPanelView> {
    }
    
    @UiField(provided=true)
    StateButton         stateButton;
    
    @UiField TextField  label;

    private final List<String> icons = new ArrayList<String>();
    private final List<ImageResource> images = new ArrayList<ImageResource>();
    
    private int         iconIndex;
    private int         imageIndex;
    private boolean     buttonPrimary = true;
    
    
    public StateButtonPanelView() {
        icons.add("icon-music");
        icons.add("icon-search");
        icons.add("icon-user");
        icons.add("icon-time");
        icons.add("icon-camera");
        icons.add("icon-plus");
        icons.add("icon-eye-open");
        
        images.add(CommonImages.INSTANCE.dialogInfo());
        images.add(CommonImages.INSTANCE.dialogQuestion());
        images.add(CommonImages.INSTANCE.dialogWarning());
        images.add(CommonImages.INSTANCE.dialogError());
        
        stateButton = new StateButton(CommonImages.INSTANCE.dialogError());
        initWidget(binder.createAndBindUi(this));
        
    }
    
    @UiHandler("btnChangeIcon")
    void onChangeIcon(ClickEvent event) {
        stateButton.setGlyphIcon(icons.get(iconIndex));
        iconIndex++;
        
        if (iconIndex >= icons.size()) {
            iconIndex = 0;
        }
    }
    
    @UiHandler("btnChangeImage")
    void onChangeImage(ClickEvent event) {
        stateButton.setImage(images.get(imageIndex));
        imageIndex++;
        
        if (imageIndex >= images.size()) {
            imageIndex = 0;
        }
    }
    
    @UiHandler("btnChangePrimary")
    void onChangePrimary(ClickEvent event) {
        stateButton.setPrimary(buttonPrimary);
        buttonPrimary = !buttonPrimary;
    }
    
    @UiHandler("btnChangeText")
    void onChangeText(ClickEvent event) {
        String newText = label.getText();
        stateButton.setText(newText);
    }
    
}
