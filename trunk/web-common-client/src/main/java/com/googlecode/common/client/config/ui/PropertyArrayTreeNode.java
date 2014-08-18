
package com.googlecode.common.client.config.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.config.schema.ArrayModel;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.panel.Confirmations;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;


public class PropertyArrayTreeNode extends BrowseTreeItem {

    private static final PropertyArrayBrowsePanel browsePanel = 
        new PropertyArrayBrowsePanel();
    
    private final Set<String>   cmdSet = new HashSet<String>();
    private final ArrayModel    model;
    private boolean             defined;
    
    
    public PropertyArrayTreeNode(ArrayModel model, boolean defined) {
        super(ObjectTreeNode.getName(model.getNode()));
        
        this.model = model;
        setDefined(defined);
    }
    
    public ArrayModel getModel() {
        return model;
    }

    public boolean isDefined() {
        return defined;
    }
    
    @Override
    public Widget getContentPanel() {
        browsePanel.setTreeNode(this);
        return browsePanel;
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return cmdSet;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (ObjectTreeNode.CMD_ADD.equals(cmd)) {
            setDefined(true);
        
        } else if (ObjectTreeNode.CMD_REMOVE.equals(cmd)) {
            Confirmations.deleteItem(new Command() {
                @Override
                public void execute() {
                    setDefined(false);
                }
            });
        }
    }
    
    private void setDefined(boolean defined) {
        this.defined = defined;
        setIcon(defined ? ConfigImages.INSTANCE.arraySelected() 
                : ConfigImages.INSTANCE.array());
        
        cmdSet.clear();
        cmdSet.add(defined ? ObjectTreeNode.CMD_REMOVE : ObjectTreeNode.CMD_ADD);
        
        BrowseTreePanel container = getContainer();
        if (container != null) {
            container.updateContent(this);
        }
    }

}
