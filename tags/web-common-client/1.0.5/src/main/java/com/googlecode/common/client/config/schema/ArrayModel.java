
package com.googlecode.common.client.config.schema;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.json.client.JSONArray;


public final class ArrayModel {

    private final PropertyArrayNode node;
    private final List<Object>      values = new ArrayList<Object>();
    
    
    public ArrayModel(PropertyArrayNode node) {
        this.node = node;
    }
    
    public ArrayModel(ArrayModel model) {
        this.node = model.getNode();
        
        for (Object val : model.getValues()) {
            values.add(val);
        }
    }
    
    public PropertyArrayNode getNode() {
        return node;
    }
    
    public void read(JSONArray jsonArr) {
        PropertyNode<?> prop = node.getItem();
        for (int i = 0, count = jsonArr.size(); i < count; i++) {
            values.add(prop.readValue(jsonArr.get(i)));
        }
    }
    
    public List<?> getValues() {
        return values;
    }
    
    public int addValue(Object val) {
        int index = values.size();
        values.add(val);
        return index;
    }
    
    public void remove(int index) {
        values.remove(index);
    }
    
    public void setValue(int index, Object val) {
        values.set(index, val);
    }
    
}
