
package com.googlecode.common.client.config.schema;


public abstract class ComplexArrayNode<T extends ComplexNode> extends 
        ArrayNode<T> {

    protected ComplexArrayNode(ComplexArrayNode<T> ref, T item) {
        super(ref, item);
    }

}
