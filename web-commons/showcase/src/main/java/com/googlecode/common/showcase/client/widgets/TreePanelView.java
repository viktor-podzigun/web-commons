
package com.googlecode.common.showcase.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.CheckBoxTree;
import com.googlecode.common.client.ui.tree.CheckBoxTreeNode;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link Tree} show-case.
 */
public final class TreePanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, TreePanelView> {
    }
    
    @UiField(provided=true) CheckBoxTree    checkTree;
    @UiField(provided=true) CheckBoxTree    readOnlyTree;

    
    public TreePanelView() {
        checkTree = new CheckBoxTree();
        fillTree(checkTree, false);
        
        readOnlyTree = new CheckBoxTree(true);
        fillTree(readOnlyTree, true);
        readOnlyTree.refreshNodesState();
        
        initWidget(binder.createAndBindUi(this));
    }
    
    private void fillTree(CheckBoxTree tree, boolean preset) {
        CheckBoxTreeNode node1 = new CheckBoxTreeNode(tree, "Node1");
        node1.setImage(ButtonImages.INSTANCE.folder());
        tree.addItem(node1);
        node1.addItem(new CheckBoxTreeNode(node1, "Item 1"));
        
        CheckBoxTreeNode item2 = new CheckBoxTreeNode(node1, "Item 2");
        item2.setImage(ButtonImages.INSTANCE.folder());
        node1.addItem(item2);
        CheckBoxTreeNode item3 = new CheckBoxTreeNode(item2, "Item 3");
        item2.addItem(item3);
        
        CheckBoxTreeNode node2 = new CheckBoxTreeNode(tree, "Node2");
        node2.setImage(ButtonImages.INSTANCE.folder());
        tree.addItem(node2);
        CheckBoxTreeNode item4 = new CheckBoxTreeNode(node2, "Item 4");
        node2.addItem(item4);
        node2.addItem(new CheckBoxTreeNode(node2, "Item 5"));
        
        if (preset) {
            item3.setNodeSelected(true);
            item4.setNodeSelected(true);
        }
    }

}
