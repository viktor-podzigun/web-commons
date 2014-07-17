
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;


/**
 * Simple combo-box widget.
 * 
 * @param <T> type of combo-box elements
 * 
 * @see LoadableComboBox
 */
public final class ComboBox<T> extends AbstractComboBox<T> {

    protected int           selectedIndex = -1;
    
    
    public ComboBox() {
        super(new ListBox());
        
        initWidget(listBox);
    
        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                onItemSelected(listBox.getSelectedIndex());
            }
        });
        listBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                final int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_DOWN
                        || keyCode == KeyCodes.KEY_UP
                        || keyCode == KeyCodes.KEY_PAGEDOWN
                        || keyCode == KeyCodes.KEY_PAGEUP
                        || keyCode == KeyCodes.KEY_HOME
                        || keyCode == KeyCodes.KEY_END) {
                    
                    onItemSelected(listBox.getSelectedIndex());
                }
            }
        });
    }
    
    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return listBox.addKeyDownHandler(handler);
    }
    
    @Override
    public boolean isEnabled() {
        return listBox.isEnabled();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        listBox.setEnabled(enabled);
    }

    @Override
    public void clear() {
        super.clear();
        selectedIndex = -1;
    }
    
    @Override
    public T getSelected() {
        if (selectedIndex == -1) {
            return null;
        }

        return elements.get(selectedIndex);
    }

    @Override
    protected void onItemSelected(int selectedIndex) {
        final int oldIndex = this.selectedIndex;
        
        this.selectedIndex = selectedIndex;
        
        if (selectCommand != null && oldIndex != selectedIndex) {
            selectCommand.execute();
        }
    }

}
