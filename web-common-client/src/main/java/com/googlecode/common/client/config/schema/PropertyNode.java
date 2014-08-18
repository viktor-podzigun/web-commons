
package com.googlecode.common.client.config.schema;

import com.google.gwt.json.client.JSONValue;


public abstract class PropertyNode<V> extends AbstractNode {

    private V   defValue;
    
    
    protected PropertyNode(PropertyNode<V> ref) {
        super(ref);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected PropertyNode<V> getRef() {
        return (PropertyNode<V>)super.getRef();
    }
    
    public abstract V readValue(JSONValue json);
    
    public abstract V parseValue(String val) throws PropertyValidationException;
    
    public V getDefault() {
        PropertyNode<V> ref = getRef();
        if (defValue == null && ref != null) {
            return ref.getDefault();
        }
        
        return defValue;
    }
    
    protected void setDefault(V def) {
        this.defValue = def;
    }
    
}
