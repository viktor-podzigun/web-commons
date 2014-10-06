
package com.googlecode.common.client.config.schema;


public final class ObjectArrayNode extends ComplexArrayNode<ObjectNode> {

    
    public ObjectArrayNode(ObjectNode node) {
        this(null, node);
    }

    private ObjectArrayNode(ObjectArrayNode ref, ObjectNode item) {
        super(ref, item);
    }
    
    @Override
    public ObjectArrayNode asRef() {
        return new ObjectArrayNode(this, getItem());
    }
    
}
