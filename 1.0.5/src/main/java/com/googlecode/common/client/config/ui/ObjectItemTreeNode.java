
package com.googlecode.common.client.config.ui;

import java.util.Collection;
import com.google.gwt.user.client.Command;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.config.schema.ObjectNode;
import com.googlecode.common.client.ui.panel.Confirmations;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;


public final class ObjectItemTreeNode extends ObjectTreeNode {

    
    public ObjectItemTreeNode(ObjectNode node) {
        this(new ObjectModel(node));
    }

    public ObjectItemTreeNode(ObjectModel model) {
        super(model, true);
    }

    @Override
    public Collection<String> getActionCommands() {
        return ObjectTreeNode.CMD_LIST;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (ObjectTreeNode.CMD_ADD.equals(cmd)) {
            onAddAction();
        
        } else if (ObjectTreeNode.CMD_REMOVE.equals(cmd)) {
            Confirmations.deleteItem(new Command() {
                @Override
                public void execute() {
                    getParent().removeAndSelect(ObjectItemTreeNode.this);
                }
            });
        }
    }
    
    private void onAddAction() {
        BrowseTreeNode parent = getParent();
        parent.addAndSelect(parent.getChildIndex(this) + 1, 
                new ObjectItemTreeNode(new ObjectModel(getModel())), 
                true, false);
    }
    
}
