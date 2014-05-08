
package com.googlecode.common.client.ui.tree;

import java.util.Collection;
import java.util.List;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;


/**
 * Abstract browse tree node with dynamically loadable children.
 */
public abstract class LoadableBrowseTreeNode extends BrowseTreeNode 
        implements LoadableTreeNode {

    private boolean     isNeedLoad = true;
    
    
    protected LoadableBrowseTreeNode(String text, Widget panel, 
            ImageResource icon) {
        
        super(text, panel, icon);
    }
    
    @Override
    protected void addDummyItem() {
        super.addDummyItem();
        setNeedLoad(true);
    }
    
    @Override
    protected boolean removeDummyItem() {
        if (super.removeDummyItem()) {
            setNeedLoad(false);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean isLeaf() {
        return false;
    }
    
    public boolean isNeedLoad() {
        return isNeedLoad;
    }
    
    public void setNeedLoad(boolean isNeedLoad) {
        this.isNeedLoad = isNeedLoad;
    }

    @Override
    public void onNodeOpen() {
        checkLoadChildren();
    }
    
    @Override
    public void onNodeClose() {
        boolean needSelect = false;
        if (!isSelected()) {
            BrowseTreeItem child = getSelectedChildR(this);
            if (child != null) {
                needSelect = true;
            }
        }
        
        isNeedLoad = true;
        removeAll();
        notifyStructureChanged();
        
        if (needSelect) {
            setSelected(true);
        }
    }
    
    private static BrowseTreeItem getSelectedChildR(BrowseTreeNode parent) {
        ListDataProvider<BrowseTreeItem> prov = parent.getDataProvider();
        List<BrowseTreeItem> list = prov.getList();
        if (list != null) {
            for (BrowseTreeItem item : list) {
                if (item.isSelected()) {
                    return item;
                }
                
                if (item instanceof BrowseTreeNode) {
                    BrowseTreeItem child = getSelectedChildR(
                            (BrowseTreeNode)item);
                    if (child != null) {
                        return child;
                    }
                }
            }
        }
        
        return null;
    }
    
    private void checkLoadChildren() {
        if (isNeedLoad) {
            isNeedLoad = false;
            
            onLoadChildren();
        }
    }
    
    @Override
    protected void onRefreshAction() {
        // refresh browse panel
        super.onRefreshAction();
        
        // refresh children
        setNeedLoad(true);
        if (isOpened()) {
            checkLoadChildren();
        }
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST_BASE;
    }

    @Override
    public abstract void onLoadChildren();

}
