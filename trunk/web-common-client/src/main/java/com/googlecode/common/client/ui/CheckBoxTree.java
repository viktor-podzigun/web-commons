
package com.googlecode.common.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.googlecode.common.client.ui.tree.CheckBoxTreeNode;


/**
 * Three-state check-box tree.
 */
public class CheckBoxTree extends Tree {

    private final boolean           isReadOnly;
    private final ValueHandler      valueHandler = new ValueHandler();
    

    public CheckBoxTree() {
        this(false);
    }
    
    public CheckBoxTree(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        updateReadOnlyState();
    }
    
    public ValueChangeHandler<Boolean> getValueHandler() {
        return valueHandler;
    }
    
    public boolean isReadOnly() {
        return isReadOnly;
    }
    
    private void updateReadOnlyState() {
//        Container parent = getParent();
//        Color readOnlyBgColor = (parent != null ? 
//                parent.getBackground() : getBackground());
//        
//        if (isReadOnly) {
//            setBackground(readOnlyBgColor);
//        } else {
//            setBackground(Color.WHITE);
//        }
    }
    
    public void expandAll() {
        for (int i = 0, count = getItemCount(); i < count; i++) {
            setStateAll(getItem(i), true);
        }
    }
    
    public void collapseAll() {
        for (int i = 0, count = getItemCount(); i < count; i++) {
            setStateAll(getItem(i), false);
        }
    }
    
    private void setStateAll(TreeItem item, boolean open) {
        item.setState(open);
        
        for (int i = 0, count = item.getChildCount(); i < count; i++) {
            setStateAll(item.getChild(i), open);
        }
    }
    
    private void onStateChanged() {
        TreeItem item = getSelectedItem();
        if (isReadOnly || item == null) {
            return;
        }

        if (item instanceof CheckBoxTreeNode) {
            CheckBoxTreeNode node = (CheckBoxTreeNode) item;
            onNodeSelection(node.getNodeState().next() == TriState.SELECTED,
                    node);
        }
    }
    
    protected void onNodeSelection(boolean isSelected, CheckBoxTreeNode node) {
        setNodeSelection(isSelected, node);
    }

    private void setNodeState(boolean isSelected, CheckBoxTreeNode node) {
        if (node == null) {
            return;
        }
        
        if (isSelected) {
            node.setNodeState(TriState.SELECTED);
        } else {
            node.setNodeState(TriState.DESELECTED);
        }
        
        int count = node.getChildCount();
        if (count <= 0) {
            return;
        }
        
        for (int i = 0; i < count; i++) {
            TreeItem treeNode = node.getChild(i);
            if (treeNode == null) {
                continue;
            }
            
            if (treeNode instanceof CheckBoxTreeNode) {
                setNodeState(isSelected, (CheckBoxTreeNode) treeNode);
            }
        }
    }
    
    public void setNodeSelection(boolean isSelected, CheckBoxTreeNode node) {
        setNodeState(isSelected, node);
        refreshNodesState();
    }
    
    public void refreshNodesState() {
        for (int i = 0, count = getItemCount(); i < count; i++) {
            TreeItem item = getItem(i);
            if (item instanceof CheckBoxTreeNode) {
                refreshNodesState((CheckBoxTreeNode) item);
            }
        }
    }
    
    private TriState refreshNodesState(CheckBoxTreeNode root) {
        TriState state = TriState.INDETERMINATE;
        int count = root.getChildCount();
        if (count <= 0) {
            root.setNodeState(state);
            return state;
        }
        
        boolean first = true;
        for (int i = 0; i < count; i++) {
            TreeItem obj = root.getChild(i);
            TriState newState = null;
            
            if (obj instanceof CheckBoxTreeNode) {
                CheckBoxTreeNode node = (CheckBoxTreeNode)obj;
                if (node.getChildCount() <= 0) {
                    newState = node.getNodeState();
                } else {
                    newState = refreshNodesState(node);
                }
            }
            
            if (newState != null) {
                if (first) {
                    state = newState;
                } else {
                    if (state != newState) {
                        state = TriState.INDETERMINATE;
                    }
                }
                first = false;
            }
        }
        
        root.setNodeState(state);
        return state;
    }


    private class ValueHandler implements ValueChangeHandler<Boolean> {
        
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            onStateChanged();
        }
    }

}
