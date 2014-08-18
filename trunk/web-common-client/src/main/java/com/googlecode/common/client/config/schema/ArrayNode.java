
package com.googlecode.common.client.config.schema;


public abstract class ArrayNode<T extends AbstractNode> extends ComplexNode {

    private final T     item;
    private int         maxItems = Integer.MAX_VALUE;
    private int         minItems;
    private boolean     uniqueItems;
    
    
    protected ArrayNode(ArrayNode<T> ref, T item) {
        super(ref);
        
        this.item = item;
    }
    
    @Override
    public JsonType getType() {
        return JsonType.ARRAY;
    }
    
    public final JsonType getArrayType() {
        return getItem().getType();
    }
    
    public abstract ArrayNode<T> asRef();
    
    @Override
    @SuppressWarnings("unchecked")
    protected ArrayNode<T> getRef() {
        return (ArrayNode<T>)super.getRef();
    }
    
    public T getItem() {
        ArrayNode<T> ref = getRef();
        if (item == null && ref != null) {
            return ref.getItem();
        }
        
        return item;
    }
    
    public int getMaxItems() {
        ArrayNode<T> ref = getRef();
        if (maxItems == Integer.MAX_VALUE && ref != null) {
            return ref.getMaxItems();
        }
        
        return maxItems;
    }
    
    protected void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public int getMinItems() {
        ArrayNode<T> ref = getRef();
        if (minItems == 0 && ref != null) {
            return ref.getMinItems();
        }
        
        return minItems;
    }
    
    protected void setMinItems(int minItems) {
        this.minItems = minItems;
    }
    
    public boolean isUniqueItems() {
        ArrayNode<T> ref = getRef();
        if (!uniqueItems && ref != null) {
            return ref.isUniqueItems();
        }
        
        return uniqueItems;
    }
    
    protected void setUniqueItems(boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }
    
}
