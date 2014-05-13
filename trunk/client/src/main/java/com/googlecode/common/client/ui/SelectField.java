
package com.googlecode.common.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;


/**
 * Text field with select button.
 */
public final class SelectField<T> extends Composite implements HasText, 
        HasClickHandlers {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, SelectField<?>> {
    }

    @UiField TextField  textField;
    @UiField Button     btnSelect;
    
    private T           id;
    
    
    public SelectField() {
        initWidget(binder.createAndBindUi(this));
    
        textField.getElement().setId("appendedInputButton");
    }
    
    public long getLongId() {
        return (Long)id;
    }

    
    public T getId() {
        return id;
    }
    
    public int getIntId() {
        return (Integer)id;
    }
    
    public void setId(T id) {
        this.id = id;
    }
    
    public void setField(String text, T id) {
        setText(text);
        setId(id);
    }
    
    /**
     * Determines whether or not the widget is read-only.
     * 
     * @return <code>true</code> if the widget is currently read-only,
     *         <code>false</code> if the widget is currently editable
     */
    public boolean isReadOnly() {
        return textField.isReadOnly();
    }
    
    /**
     * Turns read-only mode on or off.
     * 
     * @param readOnly if <code>true</code>, the widget becomes read-only; if
     *          <code>false</code> the widget becomes editable
     */
    public void setReadOnly(boolean readOnly) {
        textField.setReadOnly(readOnly);
    }
    
    public void setButtonVisible(boolean visible) {
        if (visible) {
            addStyleName("input-append");
        } else {
            removeStyleName("input-append");
        }
        
        btnSelect.setVisible(visible);
    }

    @Override
    public String getText() {
        return textField.getText();
    }

    @Override
    public void setText(String text) {
        textField.setText(text);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return btnSelect.addClickHandler(handler);
    }

}
