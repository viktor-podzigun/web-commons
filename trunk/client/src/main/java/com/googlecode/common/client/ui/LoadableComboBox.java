
package com.googlecode.common.client.ui;

import java.util.Collections;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Loadable {@link ComboBox} widget.
 * 
 * @param <T> type of combo-box elements
 */
public class LoadableComboBox<T> extends AbstractComboBox<T> {

    private static final Binder binder = GWT.create(Binder.class);
    @SuppressWarnings("rawtypes")
    interface Binder extends UiBinder<Widget, LoadableComboBox> {
    }

    @UiField TextField      textField;
    @UiField StateButton    button;

    private final PopupPanel popup  = new PopupPanel(true, true);
    
    private Command         loadCommand;
    private boolean         dataLoaded;
    
    
    public LoadableComboBox() {
        super(new ListBox(true));
        
        initWidget(binder.createAndBindUi(this));
        
        textField.getElement().setId("appendedInputButton");
        
        Element span = DOM.createSpan();
        span.setClassName("caret");
        button.getElement().appendChild(span);
        
        popup.add(listBox);
        popup.setAutoHideOnHistoryEventsEnabled(true);
        popup.addAutoHidePartner(button.getElement());
        
        textField.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                selectedIndex = -1;
            }
        });
        
        button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    if (loadCommand != null && !dataLoaded) {
                        button.setDown(false);
                        loadCommand.execute();
                    } else {
                        setListVisible(true);
                    }
                } else {
                    setListVisible(false);
                }
            }
        });
        
        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                if (event.isAutoClosed()) {
                    button.setDown(false);
                }
            }
        });
        
        listBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onItemSelected(listBox.getSelectedIndex());
            }
        });
        listBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                final int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_ENTER) {
                    onItemSelected(listBox.getSelectedIndex());
                
                } else if (keyCode == KeyCodes.KEY_ESCAPE) {
                    setListVisible(false);
                }
            }
        });
    }
    
    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return textField.addKeyDownHandler(handler);
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
    
    @Override
    public boolean isEnabled() {
        return button.isEnabled();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (!isReadOnly()) {
            textField.setEnabled(enabled);
        }
        
        button.setEnabled(enabled);
    }

    @Override
    public void clear() {
        super.clear();
        
        dataLoaded = false;
        textField.setValue("");
    }

    @Override
    protected void onItemSelected(int selectedIndex) {
        super.onItemSelected(selectedIndex);
        
        T item = getSelected();
        textField.setValue(item != null ? item.toString() : "");
        
        setListVisible(false);
    }
    
    public void setLoadCommand(Command command) {
        this.loadCommand = command;
    }
    
    public void setLoadedDataAndShow(List<T> data) {
        if (data == null) {
            data = Collections.emptyList();
        }
        
        clear();
        addAll(data);
        
        dataLoaded = true;
        setListVisible(true);
    }
    
    public String getText() {
        return textField.getText();
    }
    
    public void setListVisible(boolean isVisible) {
        if (popup.isShowing() == isVisible) {
            return;
        }
        
        if (isVisible) {
            final int count = getItemCount();
            listBox.setVisibleItemCount(count > 10 ? 10 : count);
            
            listBox.setSelectedIndex(selectedIndex != -1 ? selectedIndex : 0);
            listBox.setWidth("" + getOffsetWidth() + "px");
            popup.showRelativeTo(this);
            button.setDown(true);
            listBox.setFocus(true);
        } else {
            popup.hide();
            button.setDown(false);
            textField.setFocus(true);
        }
    }
    
}
