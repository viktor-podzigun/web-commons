
package com.googlecode.common.client.config.ui;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.config.schema.JsonSchema;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.panel.AbstractEditPanel;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.client.util.JSONHelpers;


/**
 * Configuration editing panel.
 */
public final class ConfigEditPanel extends AbstractEditPanel {

    public ConfigEditPanel(AppMainPanel appPanel, String title, JSONValue resp) {
        super(appPanel, title);
        
        ButtonsPanel buttonsPanel = getButtonsPanel();
        buttonsPanel.addButton(ButtonType.ADD);
        buttonsPanel.addButton(ButtonType.REMOVE);
        buttonsPanel.setShowText(false);
        buttonsPanel.setVisible(true);
        
        BrowseTreeNode root = getRoot();
        
        BrowseTreeNode configNode = parseData(resp);
        root.add(configNode);
        root.notifyStructureChanged();
        
        // open root node
        openAndSelectNode(configNode);
        setContentSpanWidth(6);
    }
    
    private BrowseTreeNode parseData(JSONValue resp) {
        JSONObject json   = JSONHelpers.toObject(resp);
        JSONObject schema = JSONHelpers.getObject(json, "schema");
        JSONObject data   = JSONHelpers.getObject(json, "data");
        
        return new ConfigRootTreeNode(new ObjectModel(
                JsonSchema.parse(schema)), data);
    }
    
}
