
package com.googlecode.common.client.ui.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.panel.LoadablePanel;


/**
 * Represents <tt>BrowseTree</tt> base item element.
 */
public class BrowseTreeItem implements ActionProvider {

    protected static final String       CMD_REFRESH = 
        ButtonType.REFRESH.getCommand();
    
    protected static final List<String> CMD_LIST_BASE = 
        Arrays.asList(CMD_REFRESH);

    private final Widget                contentPanel;
    
    protected BrowseTreePanel           container;
    protected BrowseTreeNode            parent;
    
    private String                      text;
    private ImageResource               icon;
    
    
    public BrowseTreeItem(String text) {
        this(text, null, null);
    }

    public BrowseTreeItem(String text, Widget panel) {
        this(text, panel, null);
    }

    public BrowseTreeItem(String text, Widget panel, ImageResource icon) {
        this.text           = text;
        this.contentPanel   = panel;
        this.icon           = icon;
    }
    
    public BrowseTreePanel getContainer() {
        return container;
    }
    
    public void setContainer(BrowseTreePanel container) {
        this.container = container;
    }
    
    public BrowseTreeNode getParent() {
        return parent;
    }
    
    public void addNotify() {
        if (parent != null) {
            this.container = parent.getContainer();
        }
    }
    
    public void removeNotify() {
        this.container = null;
    }
    
    public boolean hasContainer() {
        return (container != null);
    }
    
    private void checkContainer() {
        if (!hasContainer()) {
            throw new IllegalStateException("Item is not added to container yet");
        }
    }
    
    public boolean isSelected() {
        checkContainer();
        return container.isSelected(this);
    }
    
    public void setSelected(boolean selected) {
        checkContainer();
        container.setSelected(this, selected);
    }
    
    /**
     * Updates this item on the screen.
     * 
     * <p>Useful when item text or icon is changed.
     */
    private void update() {
        if (parent != null) {
            parent.getDataProvider().refresh();
        }
    }
    
    public void updateContent() {
        checkContainer();
        container.updateContent(this);
    }

    public Widget getContentPanel() {
        return contentPanel;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        update();
    }
    
    public ImageResource getIcon() {
        return icon;
    }
    
    public void setIcon(ImageResource icon) {
        this.icon = icon;
        update();
    }
    
    @Override
    public String toString() {
        return text;
    }
    
    public boolean isLeaf() {
        return true;
    }

    protected void onRefreshAction() {
        Widget currPanel = getContentPanel();
        if (currPanel instanceof LoadablePanel) {
            ((LoadablePanel)currPanel).onLoadData();
        }
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_REFRESH.equals(cmd)) {
            onRefreshAction();
        }
    }

    @Override
    public Collection<String> getActionCommands() {
        if (getContentPanel() instanceof LoadablePanel) {
            return CMD_LIST_BASE;
        }
        
        return null;
    }

}
