
package com.googlecode.common.client.ui.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;


/**
 * Represents <tt>BrowseTree</tt> node element.
 */
public class BrowseTreeNode extends BrowseTreeItem implements 
        Iterable<BrowseTreeItem> {

    private final ListDataProvider<BrowseTreeItem>  dataProvider = 
        new ListDataProvider<BrowseTreeItem>();

    private BrowseTreeItem      dummyItem; // used when no items were received
    private TreeNode            node;
    
    
    public BrowseTreeNode(String text) {
        this(text, null, null);
    }

    public BrowseTreeNode(String text, Widget panel) {
        this(text, panel, null);
    }

    public BrowseTreeNode(String text, Widget panel, ImageResource icon) {
        super(text, panel, icon);
    }

    @Override
    public Iterator<BrowseTreeItem> iterator() {
        return dataProvider.getList().iterator();
    }
    
    public ListDataProvider<BrowseTreeItem> getDataProvider() {
        return dataProvider;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }
    
    public boolean isOpened() {
        if (node != null) {
            return !node.isDestroyed();
        }
        
        return false;
    }
    
    public void setOpen(boolean open) {
        BrowseTreeNode p = this.parent;
        if (p == null) {
            return;
        }
        
        if (!p.isOpened()) {
            p.setOpen(open);
        }
        
        if (p.isOpened()) {
            TreeNode parent = p.node;
            int nodeIndex = getChildIndex(parent, this);
            if (nodeIndex != -1) {
                parent.setChildOpen(nodeIndex, open, true);
            }
        }
    }
    
    public int getChildIndex(BrowseTreeItem child) {
        if (node != null) {
            return getChildIndex(node, child);
        }
        
        return -1;
    }
    
    private int getChildIndex(TreeNode parent, BrowseTreeItem node) {
        int nodeIndex = -1;
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            if (parent.getChildValue(i) == node) {
                nodeIndex = i;
                break;
            }
        }
        
        return nodeIndex;
    }
    
    protected void addDummyItem() {
        dummyItem = new BrowseTreeItem("no data");
        add(dummyItem);
    }
    
    protected boolean removeDummyItem() {
        if (dummyItem != null) {
            remove(dummyItem);
            dummyItem = null;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean isLeaf() {
        return dataProvider.getList().isEmpty();
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
    
        for (BrowseTreeItem node : dataProvider.getList()) {
            node.addNotify();
        }
    }
    
    @Override
    public void removeNotify() {
        for (BrowseTreeItem node : dataProvider.getList()) {
            node.removeNotify();
        }
        
        super.removeNotify();
    }

    public void notifyStructureChanged() {
        // if this node is opened (isDestroyed() returns false)
        // we need to close (destroy) the node before refresh
        TreeNode parent = null;
        int reopenIndex = -1;
        if (container != null && node != null && !node.isDestroyed()) {
            parent = container.getParentNode(node);
            reopenIndex = node.getIndex();
            parent.setChildOpen(reopenIndex, false, false);
        }
        
        refresh();

        // reopen it, if it was opened
        if (parent != null) {
            parent.setChildOpen(reopenIndex, true, false);
        }
    }
    
    public void refresh() {
        dataProvider.refresh();
    }

    public <T extends BrowseTreeItem> T add(T node) {
        return add(-1, node);
    }
    
    public <T extends BrowseTreeItem> T add(int index, T node) {
        List<BrowseTreeItem> list = dataProvider.getList();
        if (index == -1) {
            index = list.size();
        }
        
        list.add(index, node);
        
        node.parent = this;
        if (container != null) {
            node.addNotify();
        }
        
        return node;
    }
    
    public <T extends BrowseTreeItem> T addAndSelect(T node, 
            boolean forceOpen, boolean sort) {
        
        return addAndSelect(-1, node, forceOpen, sort);
    }
        
    public <T extends BrowseTreeItem> T addAndSelect(int index, T node, 
            boolean forceOpen, boolean sort) {
        
        removeDummyItem();
        
        add(index, node);
        if (sort) {
            sortChildren();
        }
        
        if (forceOpen) {
            if (!isOpened()) {
                setOpen(true);
            }
            
            node.setSelected(true);
        } else {
            if (isOpened()) {
                node.setSelected(true);
            }
        }
        
        return node;
    }
    
    public <T extends BrowseTreeItem> void removeAndSelect(T node) {
        boolean selected = node.isSelected();
        if (!remove(node)) {
            return;
        }
        
        if (isOpened()) {
            TreeNode parent = this.node;
            final int count = parent.getChildCount();
            if (count > 1) {
                int index;
                if (selected && (index = getChildIndex(parent, node)) != -1) {
                    // if node was selected
                    if (index == (count - 1)) {
                        index--; // select previous sibling
                    } else {
                        index++; // select next sibling
                    }

                    Object item = parent.getChildValue(index);
                    if (item instanceof BrowseTreeItem) {
                        container.setSelected((BrowseTreeItem)item, true);
                    }
                }
            } else {
                // close empty parent
                setOpen(false);
                if (selected) {
                    // select it, if node was selected
                    setSelected(true);
                }
            }
        }
        
        dataProvider.refresh();
    }
    
    public <T extends BrowseTreeItem> boolean remove(T node) {
        if (dataProvider.getList().remove(node)) {
            if (container != null) {
                node.removeNotify();
                node.parent = null;
            }
            
            return true;
        }
        
        return false;
    }
    
    public void removeAll() {
        removeAll(false);
    }
    
    public void removeAll(boolean refresh) {
        dummyItem = null;
        if (container != null) {
            for (BrowseTreeItem node : dataProvider.getList()) {
                node.removeNotify();
                node.parent = null;
            }
        }
        
        dataProvider.getList().clear();
        
        if (refresh) {
            notifyStructureChanged();
            if (parent != null) {
                parent.refresh();
            }
        }
    }
    
    public void sortChildren() {
        Collections.sort(dataProvider.getList(), 
                new Comparator<BrowseTreeItem>() {
            
            @Override
            public int compare(BrowseTreeItem item1, BrowseTreeItem item2) {
                return item1.toString().compareTo(item2.toString());
            }
        });
        
        dataProvider.refresh();
    }
    
}
