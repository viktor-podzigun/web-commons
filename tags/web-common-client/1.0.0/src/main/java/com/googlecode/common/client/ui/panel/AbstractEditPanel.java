
package com.googlecode.common.client.ui.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.ImageButton;
import com.googlecode.common.client.ui.ImageLabel;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;


/**
 * Contains common functionality for editing panel.
 */
public abstract class AbstractEditPanel extends Composite {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, AbstractEditPanel> {
    }

    @UiField ImageLabel         title;
    @UiField ImageButton        btnSave;
    @UiField ImageButton        btnCancel;
    
    @UiField(provided=true)
    ButtonsPanel                buttonsPanel;
    
    @UiField(provided=true)
    BrowseTreePanel             browsePanel;
    
    @UiField SimplePanel        contentPanel;
    
    private final AppMainPanel  appPanel;
    private Command             okCommand;

    
    protected AbstractEditPanel(AppMainPanel appPanel, String titleText) {
        this.appPanel = appPanel;
        
        buttonsPanel = new ButtonsPanel();
        buttonsPanel.setVisible(false);
        
        browsePanel = new BrowseTreePanel(buttonsPanel);
        initWidget(binder.createAndBindUi(this));
        
        title.setText(titleText);
        btnSave.setImage(ButtonImages.INSTANCE.dbSave());
        btnCancel.setImage(ButtonImages.INSTANCE.cancel());
        
        browsePanel.setContentPanel(contentPanel);
    }
    
    public void openAndSelectNode(BrowseTreeNode node) {
        CellTree tree = getBrowseTree();
        TreeNode rootNode = tree.getRootTreeNode();
        for (int i = 0, count = rootNode.getChildCount(); i < count; i++) {
            if (rootNode.getChildValue(i) == node) {
                rootNode.setChildOpen(i, true);
                break;
            }
        }
        
        node.setSelected(true);
    }
    
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public BrowseTreeNode getRoot() {
        return browsePanel.getRoot();
    }
    
    public CellTree getBrowseTree() {
        return browsePanel.getBrowseTree();
    }
    
    public void setOkCommand(Command okCommand) {
        this.okCommand = okCommand;
    }
    
    @UiHandler("btnSave")
    void onBtnSaveClick(ClickEvent event) {
        if (okCommand != null) {
            okCommand.execute();
        }
    }
    
    @UiHandler("btnCancel")
    void onBtnCancelClick(ClickEvent event) {
        hide();
    }
    
    public void show() {
        appPanel.showPanel(this);
    }
    
    public void hide() {
        appPanel.showPanel(appPanel.getBrowsePanel());
    }
    
}
