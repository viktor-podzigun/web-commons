
package com.googlecode.common.client.ui.tree;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.TreeItem;
import com.googlecode.common.client.ui.CheckBoxTree;
import com.googlecode.common.client.ui.ImageCheckBox;
import com.googlecode.common.client.ui.TriState;


/**
 * <code>CheckBoxTree</code>'s node that has children and can take one of three 
 * states: selected, deselected or indeterminate.
 */
public class CheckBoxTreeNode extends TreeItem {

    private final CheckBoxTree  tree;
    private final ImageCheckBox checkbox = new ImageCheckBox();
    
    
    public CheckBoxTreeNode(CheckBoxTreeNode parent, String text) {
        this(parent.tree, text);
    }
    
    public CheckBoxTreeNode(CheckBoxTree tree, String text) {
        this.tree = tree;
        
        checkbox.setReadOnly(tree.isReadOnly());
        checkbox.addValueChangeHandler(tree.getValueHandler());
        
        setWidget(checkbox);
        setText(text);
    }
    
    public boolean isNodeSelected() {
        return (getNodeState() == TriState.SELECTED);
    }
    
    public void setNodeSelected(boolean state) {
        setNodeState(state ? TriState.SELECTED : TriState.DESELECTED);
    }
    
    public TriState getNodeState() {
        return checkbox.getState();
    }
    
    public void setNodeState(TriState state) {
        checkbox.setState(state);
    }

    @Override
    public String getText() {
        return checkbox.getText();
    }
    
    @Override
    public void setText(String text) {
        checkbox.setText(text);
    }

    public void setImage(ImageResource resource) {
        checkbox.setImage(resource);
    }

}
