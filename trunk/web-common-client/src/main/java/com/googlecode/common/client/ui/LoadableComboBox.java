
package com.googlecode.common.client.ui;

import java.util.ArrayList;
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
    
    private List<T>         selectedObjs = new ArrayList<T>();
    private List<T>         selObjsBeforeShow;
    private Command         loadCommand;
    private boolean         dataLoaded;
    
    public LoadableComboBox() {
        this(false);
    }
    
    public LoadableComboBox(final boolean isMultipleSelect) {
        super(new ListBox(isMultipleSelect));
        
        initWidget(binder.createAndBindUi(this));
        
        textField.getElement().setId("appendedInputButton");
        
        Element span = DOM.createSpan();
        span.setClassName("caret");
        button.getElement().appendChild(span);
        
        popup.add(listBox);
        popup.setAutoHideOnHistoryEventsEnabled(true);
        popup.addAutoHidePartner(button.getElement());
        
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
                button.setDown(false);
                textField.setFocus(true);
                
                if (selectCommand != null && (selObjsBeforeShow.size() != selectedObjs.size() 
                        || !selObjsBeforeShow.containsAll(selectedObjs))) {
                    
                    selectCommand.execute();
                }
            }
        });
        
        listBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!isMultipleSelect) {
                    setListVisible(false);
                }
            }
        });
        
        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                onItemSelected(0);
            }
        });
        
        listBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                final int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_ENTER) {
                    onItemSelected(0);
                    setListVisible(false);
                } else if (keyCode == KeyCodes.KEY_ESCAPE) {
                    setListVisible(false);
                }
            }
        });
    }
    
    @Override
    public void addStyleDependentName(String styleSuffix) {
        textField.addStyleDependentName(styleSuffix);
    }
    
    @Override
    public void addStyleName(String style) {
        textField.addStyleName(style);
    }
    
    @Override
    public void setStyleDependentName(String styleSuffix, boolean add) {
        textField.setStyleDependentName(styleSuffix, add);
    }
    @Override
    public void setStyleName(String style) {
        textField.setStyleName(style);
    }
    @Override
    public void setStyleName(String style, boolean add) {
        textField.setStyleName(style, add);
    }
    
    @Override
    public void setStylePrimaryName(String style) {
        textField.setStylePrimaryName(style);
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
        
        selectedObjs.clear();
        dataLoaded = false;
        textField.setValue("");
    }

    @Override
    protected void onItemSelected(int selectedIndex) {
        List<T> items = getSelectedObjects();
        if ((items.size() != selectedObjs.size() || !items.containsAll(selectedObjs))) {
            selectedObjs = items;
        }
        
        StringBuilder sb = new StringBuilder();
        for (T item : items) {
            if (item != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                
                sb.append(item.toString());
            }
        }
        
        textField.setValue(sb.toString());
    }
    
    @Override
    public T getSelected() {
        if (selectedObjs.isEmpty()) {
            return null;
        }
        
        // returns first element
        return selectedObjs.get(0);
    }
    
    public List<T> getSelectedList() {
        return selectedObjs;
    }
    
    public void setInitValueList(List<T> items) {
        for (T item : items) {
            if (!elements.contains(item)) {
                addItem(item);
            }
            
            listBox.setItemSelected(elements.indexOf(item), true);
        }
        
        onItemSelected(0);
        if (selectCommand != null) {
            selectCommand.execute();
        }
    }
    
    @Override
    public int setSelected(T value) {
        final int newIndex = elements.indexOf(value);
        if (newIndex == -1) {
            return -1;
        }
        
        // selects only this item, other selected items (if such exist) becomes deselected
        listBox.setSelectedIndex(newIndex);
        
        onItemSelected(0);
        if (selectCommand != null) {
            selectCommand.execute();
        }
        
        return newIndex;
    }
    
    /**
     * Selects only listed items in list (all items selected before and are 
     * absent in list will be deselected).
     * 
     * @param items     list of objects need to select
     */
    public void setSelectedList(List<T> items) {
        for (int index = 0; index < listBox.getItemCount(); index++) {
            T item = elements.get(index);
            listBox.setItemSelected(index, items.indexOf(item) != -1);
        }
        
        onItemSelected(0);
        if (selectCommand != null) {
            selectCommand.execute();
        }
    }
    
    private List<T> getSelectedObjects() {
        List<T> result = new ArrayList<T>();
        
        for (int index = 0; index < listBox.getItemCount(); index++) {
            if (listBox.isItemSelected(index)) {
                result.add(elements.get(index)); 
            }
        }
        
        return result;
    }
    
    public void setLoadCommand(Command command) {
        this.loadCommand = command;
    }
    
    @Override
    public void addItem(T item) {
        boolean needToResetSelection = !listBox.isMultipleSelect() 
                && listBox.getSelectedIndex() == -1;

        super.addItem(item);

        if (needToResetSelection) {
            // we will have a selected first element if we will not do this
            listBox.setSelectedIndex(-1);
        }
    }
    
    @Override
    public void addAll(List<T> items) {
        boolean needToResetSelection = !listBox.isMultipleSelect() 
                && listBox.getSelectedIndex() == -1;
        
        if (items == null) {
            items = Collections.emptyList();
        }
        
        for (T item : items) {
            int index = elements.indexOf(item);
            if (index != -1) {
                elements.remove(index);
                listBox.removeItem(index);
            }
            
            elements.add(item);
            listBox.addItem(item != null ? item.toString() : "");
        }
        
        if (needToResetSelection) {
            // we will have a selected first element if we will not do this
            listBox.setSelectedIndex(-1);
        }
    }
    
    public void setLoadedDataAndShow(List<T> data) {
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
            listBox.setVisibleItemCount(count > 10 ? 10 : (count < 2 ? 2 : count));
            selObjsBeforeShow = new ArrayList<T>(selectedObjs);
            
            for (T obj : selectedObjs) {
                int index = elements.indexOf(obj);
                listBox.setItemSelected(index, true);
            }
            
            listBox.setWidth("" + getOffsetWidth() + "px");
            popup.showRelativeTo(this);
            button.setDown(true);
            listBox.setFocus(true);
            
        } else {
            popup.hide();
        }
    }
    
}
