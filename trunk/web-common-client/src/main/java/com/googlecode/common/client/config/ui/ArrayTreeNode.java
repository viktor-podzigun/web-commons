
package com.googlecode.common.client.config.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.config.schema.AbstractNode;
import com.googlecode.common.client.config.schema.ArrayModel;
import com.googlecode.common.client.config.schema.ArrayNode;
import com.googlecode.common.client.config.schema.ObjectNode;
import com.googlecode.common.client.config.schema.PropertyArrayNode;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.panel.Confirmations;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;


public final class ArrayTreeNode extends BrowseTreeNode {

    private final Set<String>   cmdSet = new HashSet<String>();
    private final ArrayNode<?>  node;
    private final boolean       isArrayItem;
    
    
    public ArrayTreeNode(ArrayNode<?> node, boolean isArrayItem) {
        super(ObjectTreeNode.getName(node), null, 
                ConfigImages.INSTANCE.array());
    
        this.node        = node;
        this.isArrayItem = isArrayItem;
        
        cmdSet.add(ObjectTreeNode.CMD_ADD);
        updateState();
    }

    public ArrayNode<?> getArrayNode() {
        return node;
    }
    
    @Override
    public Widget getContentPanel() {
        return new ArrayBrowsePanel(this);
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return cmdSet;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (ObjectTreeNode.CMD_ADD.equals(cmd)) {
            onAddAction();
        
        } else if (ObjectTreeNode.CMD_REMOVE.equals(cmd)) {
            Confirmations.deleteItem(new Command() {
                @Override
                public void execute() {
                    BrowseTreeNode parent = getParent();
                    if (parent instanceof ArrayTreeNode) {
                        parent.removeAndSelect(ArrayTreeNode.this);
                    } else {
                        removeAll(true);
                        updateState();
                    }
                }
            });
        }
    }
    
    @Override
    public <T extends BrowseTreeItem> T add(int index, T node) {
        super.add(index, node);
        
        updateState();
        return node;
    }
    
    @Override
    public <T extends BrowseTreeItem> void removeAndSelect(T node) {
        super.removeAndSelect(node);
        
        updateState();
    }
    
    private void updateState() {
        List<BrowseTreeItem> list = getDataProvider().getList();
        int i = 0;
        for (BrowseTreeItem item : list) {
            item.setText("[" + (i++) + "]");
        }

        boolean update;
        boolean isEmpty = list.isEmpty();
        if (!isEmpty || isArrayItem) {
            update = cmdSet.add(ObjectTreeNode.CMD_REMOVE);
        } else {
            update = cmdSet.remove(ObjectTreeNode.CMD_REMOVE);
        }
        
        if (update) {
            setIcon(!isEmpty || isArrayItem ? 
                    ConfigImages.INSTANCE.arraySelected() 
                    : ConfigImages.INSTANCE.array());
            
            BrowseTreePanel container = getContainer();
            if (container != null) {
                container.updateContent(this);
            }
        }
    }
    
    private void onAddAction() {
        BrowseTreeItem node = null;
        AbstractNode item = getArrayNode().getItem();
        if (item instanceof ObjectNode) {
            node = new ObjectItemTreeNode((ObjectNode)item);
        
        } else if (item instanceof PropertyArrayNode) {
            node = new ArrayItemTreeNode(new ArrayModel(
                    (PropertyArrayNode)item));
        
        } else if (item instanceof ArrayNode) {
            node = new ArrayTreeNode((ArrayNode<?>)item, true);
        }
        
        addAndSelect(node, true, false);
    }
    
}
