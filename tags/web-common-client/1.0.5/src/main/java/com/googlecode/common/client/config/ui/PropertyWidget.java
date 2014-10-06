
package com.googlecode.common.client.config.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.config.schema.BooleanNode;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.config.schema.PropertyNode;
import com.googlecode.common.client.config.schema.PropertyValidationException;
import com.googlecode.common.client.ui.ImageCheckBox;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.ui.text.TextChangeEvent;
import com.googlecode.common.client.ui.text.TextChangeEventHandler;
import com.googlecode.common.client.util.StringHelpers;


public final class PropertyWidget extends Composite implements 
        ValueChangeHandler<Boolean>, TextChangeEventHandler, FocusHandler {

    private static final Binder binder = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<Widget, PropertyWidget> {
    }

    @UiField Element                    name;
    @UiField(provided=true) FocusWidget widget;
    @UiField Element                    error;
    
    private final ObjectBrowsePanel     panel;
    private final PropertyNode<?>       prop;
    
    
    public PropertyWidget(ObjectBrowsePanel panel, PropertyNode<?> prop) {
        this.panel = panel;
        this.prop  = prop;
        
        createWidget();
    }
    
    public void setEnabled(boolean enabled) {
        widget.setEnabled(enabled);
    }

    private void createWidget() {
        String label = prop.getTitle();
        if (label == null) {
            label = prop.getKey();
        }
        
        ObjectModel model = panel.getModel();
        if (prop instanceof BooleanNode) {
            BooleanNode boolProp = (BooleanNode)prop;
            ImageCheckBox checkBox = new ImageCheckBox();
            checkBox.setText(label);
            checkBox.setValue(model.getValue(boolProp));
            checkBox.addValueChangeHandler(this);
            label  = null;
            widget = checkBox;
        
        } else {
            TextField input = new TextField();
            input.setText(String.valueOf(model.getValue(prop)));
            input.addStyleName("span12");
            input.addTextChangeEventHandler(this);
            widget = input;
        }
        
        if (panel != null) {
            widget.addDomHandler(this, FocusEvent.getType());
        }
        
        initWidget(binder.createAndBindUi(this));
        
        if (StringHelpers.isNullOrEmpty(label)) {
            UIObject.setVisible(name, false);
        } else {
            name.setInnerText(label);
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        panel.showInfoPanel(prop);
    }
    
    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        ObjectModel model = panel.getModel();
        model.setValue((BooleanNode)prop, ((ImageCheckBox)widget).getValue());
    }

    @Override
    public void onTextChange(TextChangeEvent event) {
        ObjectModel model = panel.getModel();
        String inputVal = ((TextField)widget).getText();
        if (hasError() || !StringHelpers.isEqual(inputVal, 
                String.valueOf(model.getValue(prop)))) {
            
            try {
                Object val = prop.parseValue(inputVal);
                model.setValue(prop, val);
                setError(null);
                
            } catch (PropertyValidationException x) {
                setError(x.getMessage());
            }
        }
    }
    
    private boolean hasError() {
        return UIObject.isVisible(error);
    }
    
    private void setError(String message) {
        Widget widget = getWidget();
        if (message != null) {
            widget.addStyleName("error");
            error.setInnerText(message);
            UIObject.setVisible(error, true);
        } else {
            widget.removeStyleName("error");
            error.setInnerText("");
            UIObject.setVisible(error, false);
        }
    }

}
