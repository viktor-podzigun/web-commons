
package com.googlecode.common.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.client.ui.tree.BrowseTreeItem;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.client.ui.tree.LoadableTreeNode;


/**
 * Contains browse tree, control buttons, and content panel.
 */
public final class BrowseTreePanel extends Composite {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, BrowseTreePanel> {
    }
    
    @UiField(provided=true) CellTree    browseTree;

    private final ButtonsPanel          buttonsPanel;
    private final AbstractSelectionModel<BrowseTreeItem> selectionModel;
    private final BrowseTreeNode        root;
    
    private Panel                       contentPanel;
    private Widget                      currPanel;
    
    
    public BrowseTreePanel() {
        this(null);
    }
    
    public BrowseTreePanel(ButtonsPanel buttonsPanel) {
        final SingleSelectionModel<BrowseTreeItem> selectionModel = 
            new SingleSelectionModel<BrowseTreeItem>();

        this.buttonsPanel   = buttonsPanel;
        this.selectionModel = selectionModel;
        
        this.root = new BrowseTreeNode("Root");
        root.setContainer(this);
        
        this.browseTree = new CellTree(new BrowseTreeViewModel(selectionModel), 
                null);
        
        selectionModel.addSelectionChangeHandler(
                new SelectionChangeEvent.Handler() {
            
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                updateContent(selectionModel.getSelectedObject());
            }
        });
        
        // add open/close handlers for dynamic loading/removing children
        browseTree.addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                // clear an "overflow: hidden" style from wrapper nodes' DIVs
                NodeList<Element> divs = browseTree.getElement()
                        .getElementsByTagName("div");
                for (int i = divs.getLength() - 1; i >= 0; i--) {
                    divs.getItem(i).getStyle().clearOverflow();
                }
                
                onNodeOpen(event.getTarget());
            }
        });
        browseTree.addCloseHandler(new CloseHandler<TreeNode>() {
            @Override
            public void onClose(CloseEvent<TreeNode> event) {
                onNodeClose(event.getTarget());
            }
        });
        
        initWidget(binder.createAndBindUi(this));
    }
    
    public void setContentPanel(Panel contentPanel) {
        this.contentPanel = contentPanel;
    }
    
    public CellTree getBrowseTree() {
        return browseTree;
    }

    public BrowseTreeNode getRoot() {
        return root;
    }
    
    public TreeNode getParentNode(TreeNode node) {
        TreeNode parent = node.getParent();
        if (parent == null) {
            parent = browseTree.getRootTreeNode();
        }
        
        return parent;
    }
    
    public boolean isSelected(BrowseTreeItem item) {
        return selectionModel.isSelected(item);
    }
    
    public void setSelected(BrowseTreeItem item, boolean selected) {
        selectionModel.setSelected(item, selected);
    }
    
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }
    
    private void setButtonsActionProvider(ActionProvider prov) {
        if (buttonsPanel != null) {
            buttonsPanel.setActionProvider(prov);
        }
    }
    
    public void updateContent(BrowseTreeItem item) {
        if (item instanceof ActionProvider) {
            setButtonsActionProvider((ActionProvider)item);
        } else {
            setButtonsActionProvider(null);
        }
        
        if (currPanel instanceof LoadablePanel) {
            ((LoadablePanel)currPanel).onDeactivated();
        }
        
        Widget panel = null;
        if (item != null) {
            panel = item.getContentPanel();
        }
        
        if (panel != currPanel) {
            if (currPanel != null) {
                currPanel.removeFromParent();
            }
            
            currPanel = panel;
            if (currPanel != null && contentPanel != null) {
                contentPanel.add(currPanel);
            }
        }
        
        if (currPanel instanceof LoadablePanel) {
            ((LoadablePanel)currPanel).onActivated();
        }
    }
    
    private void onNodeOpen(TreeNode node) {
        Object item = node.getValue();
        if (item instanceof BrowseTreeNode) {
            ((BrowseTreeNode)item).setNode(node);
        }
        
        if (item instanceof LoadableTreeNode) {
            ((LoadableTreeNode)item).onNodeOpen();
        }
    }

    private void onNodeClose(TreeNode node) {
        Object item = node.getValue();
        if (item instanceof LoadableTreeNode) {
            ((LoadableTreeNode)item).onNodeClose();
        }
    }

    
    /**
     * The cell used to render browse tree nodes.
     */
    private static class BrowseTreeCell extends AbstractCell<BrowseTreeItem> {

        @Override
        public void render(Context context, BrowseTreeItem value, 
                SafeHtmlBuilder sb) {
            
            if (value != null) {
                ImageResource image = value.getIcon();
                if (image != null) {
                    sb.appendHtmlConstant(AbstractImagePrototype.create(
                            image).getHTML()).appendEscaped(" ");
                }
                
                sb.appendEscaped(value.getText());
            }
        }
    }
    
    
    private class BrowseTreeViewModel implements TreeViewModel {

        private final AbstractSelectionModel<BrowseTreeItem> selectionModel;
        
        
        public BrowseTreeViewModel(
                AbstractSelectionModel<BrowseTreeItem> selectionModel) {
            
            this.selectionModel = selectionModel;
        }
        
        @Override
        public <T> NodeInfo<?> getNodeInfo(T value) {
            if (value == null) {
                @SuppressWarnings("unchecked")
                T v = (T)root;
                value = v;
            }
            
            if (value instanceof BrowseTreeNode) {
                return new DefaultNodeInfo<BrowseTreeItem>(
                        ((BrowseTreeNode)value).getDataProvider(), 
                        new BrowseTreeCell(), selectionModel, null);
            }
        
            throw new IllegalArgumentException("Unsupported value: " + value);
        }

        @Override
        public boolean isLeaf(Object value) {
            if (value instanceof BrowseTreeItem) {
                return ((BrowseTreeItem)value).isLeaf();
            }
            
            return false;
        }
    }
    
}
