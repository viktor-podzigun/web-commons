
package com.googlecode.common.client.config.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.config.schema.NumberNode;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.config.schema.ObjectNode;
import com.googlecode.common.client.config.schema.PropertyNode;
import com.googlecode.common.client.ui.ImageLabel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.client.util.StringHelpers;


public final class ObjectBrowsePanel extends LoadablePanel {

    private static final Binder binder = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<Widget, ObjectBrowsePanel> {
    }

    @UiField ImageLabel             description;
    @UiField FlowPanel              dataPanel;
    @UiField SimplePanel            infoPanel;
    
    private ObjectTreeNode          currNode;
    
    
    public ObjectBrowsePanel() {
        initWidget(binder.createAndBindUi(this));
    }
    
    public ObjectModel getModel() {
        return (currNode != null ? currNode.getModel() : null);
    }
    
    public void setTreeNode(ObjectTreeNode node) {
        this.currNode = node;
    }
    
    @Override
    public void onLoadData() {
        fillData(getModel().getNode());
        boolean enabled = currNode.isDefined();
        for (int i = 0, count = dataPanel.getWidgetCount(); i < count; i++) {
            ((PropertyWidget)dataPanel.getWidget(i)).setEnabled(enabled);
        }
    }

    private void fillData(ObjectNode objNode) {
        dataPanel.clear();
        infoPanel.clear();
        
        String desc = objNode.getDescription();
        if (desc == null) {
            desc = StringHelpers.str(ObjectTreeNode.getName(objNode));
        }
        
        description.setText(desc);
        
        for (PropertyNode<?> prop : getModel().getProperties()) {
            dataPanel.add(new PropertyWidget(this, prop));
        }
    }
    
    void showInfoPanel(PropertyNode<?> prop) {
        infoPanel.clear();
        
        StringBuilder html = new StringBuilder("<dl>");
        html.append("<dt>Type</dt><dd>").append(prop.getType())
                .append("</dd>");

        addPropertyInfo(html, prop);
        
        html.append("</dl>");
        infoPanel.setWidget(new HTMLPanel(html.toString()));
    }
    
    public static void addPropertyInfo(StringBuilder html, 
            PropertyNode<?> prop) {
    
        Object def = prop.getDefault();
        if (def != null) {
            html.append("<dt>Default</dt><dd>").append(def)
                    .append("</dd>");
        }
        
        if (prop instanceof NumberNode) {
            NumberNode numProp = (NumberNode)prop;
            Number min = numProp.getMinimum();
            if (min != null) {
                html.append("<dt>Minimum</dt><dd>").append(min)
                        .append("</dd>");
            }
        
            Number max = numProp.getMaximum();
            if (max != null) {
                html.append("<dt>Maximum</dt><dd>").append(max)
                        .append("</dd>");
            }
        }
        
        String desc = prop.getDescription();
        if (desc != null) {
            html.append("<dt>Description</dt><dd>").append(desc)
                    .append("</dd>");
        }
    }
    
}
