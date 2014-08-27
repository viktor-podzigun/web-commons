
package com.googlecode.common.client.config.ui;

import com.google.gwt.json.client.JSONObject;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.config.schema.JsonSchema;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.panel.AbstractEditPanel;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;


/**
 * Configuration editing panel.
 */
public final class ConfigEditPanel extends AbstractEditPanel {

    public ConfigEditPanel(AppMainPanel appPanel, String title, 
            JSONObject schema, JSONObject data) {
        
        super(appPanel, title);
        
        ButtonsPanel buttonsPanel = getButtonsPanel();
        buttonsPanel.addButton(ButtonType.ADD);
        buttonsPanel.addButton(ButtonType.REMOVE);
        buttonsPanel.setShowText(false);
        buttonsPanel.setVisible(true);
        
        BrowseTreeNode root = getRoot();
        
        BrowseTreeNode configNode = new ConfigRootTreeNode(new ObjectModel(
                JsonSchema.parse(schema)), data);
        root.add(configNode);
        root.notifyStructureChanged();
        
        // open root node
        openAndSelectNode(configNode);
        setContentSpanWidth(6);
    }
    
}
