
package com.googlecode.common.client.config.ui;

import java.util.Collection;
import java.util.Collections;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.googlecode.common.client.config.schema.ArrayArrayNode;
import com.googlecode.common.client.config.schema.ArrayModel;
import com.googlecode.common.client.config.schema.ArrayNode;
import com.googlecode.common.client.config.schema.ComplexNode;
import com.googlecode.common.client.config.schema.ObjectArrayNode;
import com.googlecode.common.client.config.schema.ObjectModel;
import com.googlecode.common.client.config.schema.ObjectNode;
import com.googlecode.common.client.config.schema.PropertyArrayNode;
import com.googlecode.common.client.config.schema.PropertyNode;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.client.util.JSONHelpers;


public final class ConfigRootTreeNode extends ObjectTreeNode {

    public ConfigRootTreeNode(ObjectModel model, JSONObject json) {
        super(model, true);
    
        addObjectNode(this, model, json);
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return Collections.emptyList();
    }

    static void addObjectNode(BrowseTreeNode parent, ObjectModel model, 
            JSONObject json) {
        
        if (json != null) {
            for (PropertyNode<?> prop : model.getNode().getProperties()) {
                model.setValue(prop, prop.readValue(json.get(prop.getKey())));
            }
        }

        for (ComplexNode n : model.getNode().getNodes()) {
            if (n instanceof ObjectNode) {
                ObjectModel m = new ObjectModel((ObjectNode)n);
                JSONObject jsonObj = null;
                if (json != null) {
                    jsonObj = JSONHelpers.getObject(json, n.getKey());
                }
                
                ObjectTreeNode node = parent.add(new ObjectTreeNode(m, 
                        jsonObj != null));
                
                if (jsonObj != null) {
                    addObjectNode(node, m, jsonObj);
                }
            } else {
                JSONArray jsonArr = null;
                if (json != null) {
                    jsonArr = JSONHelpers.getArray(json, n.getKey());
                }
                
                if (n instanceof PropertyArrayNode) {
                    addPropertyArrayNode(parent, (PropertyArrayNode)n, jsonArr);
                } else {
                    addArrayNode(parent, (ArrayNode<?>)n, jsonArr);
                }
            }
        }
    }
    
    private static void addPropertyArrayNode(BrowseTreeNode parent, 
            PropertyArrayNode propArr, JSONArray jsonArr) {
        
        ArrayModel arrModel = new ArrayModel(propArr);
        boolean defined = false;
        if (jsonArr != null) {
            defined = true;
            arrModel.read(jsonArr);
        }
        
        if (parent instanceof ArrayTreeNode) {
            parent.add(new ArrayItemTreeNode(arrModel));
        } else {
            parent.add(new PropertyArrayTreeNode(arrModel, defined));
        }
    }
    
    private static void addArrayNode(BrowseTreeNode parent, ArrayNode<?> arr, 
            JSONArray jsonArr) {
        
        if (arr instanceof PropertyArrayNode) {
            addPropertyArrayNode(parent, (PropertyArrayNode)arr, jsonArr);
            return;
        }
    
        ArrayTreeNode node = parent.add(new ArrayTreeNode(arr, 
                parent instanceof ArrayTreeNode));
        if (jsonArr == null) {
            return;
        }
        
        if (arr instanceof ObjectArrayNode) {
            ObjectArrayNode objArr = (ObjectArrayNode)arr;
            ObjectNode objItem = objArr.getItem();
            for (int i = 0, count = jsonArr.size(); i < count; i++) {
                JSONObject jsonItem = JSONHelpers.toObject(jsonArr.get(i));
                if (jsonItem != null) {
                    ObjectModel m = new ObjectModel(objItem);
                    ObjectItemTreeNode item = node.add(new ObjectItemTreeNode(m));
                    addObjectNode(item, m, jsonItem);
                }
            }
        } else if (arr instanceof ArrayArrayNode) {
            ArrayArrayNode arrArr = (ArrayArrayNode)arr;
            ArrayNode<?> arrItem = arrArr.getItem();
            for (int i = 0, count = jsonArr.size(); i < count; i++) {
                addArrayNode(node, arrItem, JSONHelpers.toArray(jsonArr.get(i)));
            }
        }
    }
    
}
