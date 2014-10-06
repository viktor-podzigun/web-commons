
package com.googlecode.common.client.config.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.config.schema.AbstractNode;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.Confirmations;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;


public class ObjectTreeNode extends BrowseTreeNode {

    static final String         CMD_ADD     = ButtonType.ADD.getCommand();
    static final String         CMD_REMOVE  = ButtonType.REMOVE.getCommand();
    static final List<String>   CMD_LIST    = Arrays.asList(CMD_ADD, CMD_REMOVE);

    private static final ObjectBrowsePanel browsePanel = 
        new ObjectBrowsePanel();
    
    private final Set<String>   cmdSet      = new HashSet<String>();
    private final ObjectModel   model;
    private boolean             defined;
    
    
    public ObjectTreeNode(ObjectModel model, boolean defined) {
        super(getName(model.getNode()));
        
        this.model = model;
        
        setDefined(defined);
    }
    
    public static String getName(AbstractNode node) {
        return (node.getTitle() == null ? node.getKey() : node.getTitle());
    }
    
    public ObjectModel getModel() {
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
        if (CMD_ADD.equals(cmd)) {
            ConfigRootTreeNode.addObjectNode(this, model, null);
            if (!isLeaf()) {
                setOpen(true);
            }
            
            setDefined(true);
        
        } else if (CMD_REMOVE.equals(cmd)) {
            Confirmations.deleteItem(new Command() {
                @Override
                public void execute() {
                    removeAll(true);
                    setDefined(false);
                }
            });
        }
    }
    
    private void setDefined(boolean defined) {
        this.defined = defined;
        setIcon(defined ? ConfigImages.INSTANCE.objectSelected() 
                : ConfigImages.INSTANCE.object());
        
        cmdSet.clear();
        cmdSet.add(defined ? CMD_REMOVE : CMD_ADD);
        
        BrowseTreePanel container = getContainer();
        if (container != null) {
            container.updateContent(this);
        }
    }

}
