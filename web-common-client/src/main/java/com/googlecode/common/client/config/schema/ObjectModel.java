
package com.googlecode.common.client.config.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class ObjectModel {

    private final ObjectNode            node;
    private final Map<String, Object>   values;
    
    
    public ObjectModel(ObjectNode node) {
        this.node   = node;
        this.values = new HashMap<String, Object>(node.getProperties().size());
        
        for (PropertyNode<?> prop : node.getProperties()) {
            values.put(prop.getKey(), prop.getDefault());
        }
    }
    
    public ObjectModel(ObjectModel model) {
        this.node   = model.getNode();
        this.values = new HashMap<String, Object>(node.getProperties().size());
        
        for (PropertyNode<?> prop : node.getProperties()) {
            values.put(prop.getKey(), model.getValue(prop));
        }
    }
    
    public ObjectNode getNode() {
        return node;
    }
    
    public List<PropertyNode<?>> getProperties() {
        return node.getProperties();
    }
    
    @SuppressWarnings("unchecked")
    public <V> V getValue(PropertyNode<V> prop) {
        return (V)values.get(prop.getKey());
    }
    
    public void setValue(PropertyNode<?> prop, Object value) {
        values.put(prop.getKey(), value);
    }
    
}
