
package com.googlecode.common.client.config.schema;


public final class PropertyArrayNode extends ArrayNode<PropertyNode<?>> {

    public PropertyArrayNode(PropertyNode<?> node) {
        this(null, node);
    }

    private PropertyArrayNode(PropertyArrayNode ref, PropertyNode<?> item) {
        super(ref, item);
    }
    
    @Override
    public PropertyArrayNode asRef() {
        return new PropertyArrayNode(this, getItem());
    }
    
}
