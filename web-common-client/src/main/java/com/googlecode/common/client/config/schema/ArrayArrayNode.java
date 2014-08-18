
package com.googlecode.common.client.config.schema;


public final class ArrayArrayNode extends ComplexArrayNode<ArrayNode<?>> {

    public ArrayArrayNode(ArrayNode<?> node) {
        this(null, node);
    }

    private ArrayArrayNode(ArrayArrayNode ref, ArrayNode<?> item) {
        super(ref, item);
    }
    
    @Override
    public ArrayArrayNode asRef() {
        return new ArrayArrayNode(this, getItem());
    }
    
}
