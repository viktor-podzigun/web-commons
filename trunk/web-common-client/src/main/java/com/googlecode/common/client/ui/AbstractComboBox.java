
package com.googlecode.common.client.ui;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ListBox;


/**
 * Contains common functionality for combo-box.
 * 
 * @param <T> type of combo-box elements
 */
public abstract class AbstractComboBox<T> extends Composite implements 
        HasEnabled, HasKeyDownHandlers {

    protected final ListBox listBox;
    protected final List<T> elements = new ArrayList<T>();
    
    protected Command       selectCommand;

    
    protected AbstractComboBox(ListBox listBox) {
        this.listBox = listBox;
    }
    
    public void setSelectCommand(Command command) {
        this.selectCommand = command;
    }
    
    /**
     * Gets the number of items present in the list box.
     * 
     * @return the number of items
     */
    public int getItemCount() {
        return listBox.getItemCount();
    }
    
    public void setInitValue(T value) {
        if (!elements.contains(value)) {
            addItem(value);
        }
        
        setSelected(value);
    }
    
    public void addAll(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }
    
    public void addItem(T item) {
        elements.add(item);
        listBox.addItem(item != null ? item.toString() : "");
    }

    /**
     * Removes the specified item.
     *
     * @param value the item to be removed
     * @return      the index of the removed item, or <code>null</code> 
     *              if no such found
     */
    public int removeItem(T value) {
        final int index = elements.indexOf(value);
        if (index != -1) {
            listBox.removeItem(index);
            elements.remove(index);
        }
        
        return index;
    }
    
    public void clear() {
        elements.clear();
        listBox.clear();
    }
    
    public int setSelected(T value) {
        final int newIndex = elements.indexOf(value);
        if (newIndex == -1) {
            return -1;
        }
        
        listBox.setSelectedIndex(newIndex);
        onItemSelected(newIndex);
        return newIndex;
    }
    
    public abstract T getSelected();
    
    protected abstract void onItemSelected(int selectedIndex);
    
}
