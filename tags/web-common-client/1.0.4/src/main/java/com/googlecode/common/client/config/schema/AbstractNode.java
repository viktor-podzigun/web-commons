
package com.googlecode.common.client.config.schema;


public abstract class AbstractNode {

    private final AbstractNode  ref;
    
    private String      key;
    private String      title;
    private String      description;
    
    
    protected AbstractNode(AbstractNode ref) {
        this.ref = ref;
    }
    
    protected AbstractNode getRef() {
        return ref;
    }
    
    public abstract JsonType getType();
    
    public abstract AbstractNode asRef();
    
    public String getKey() {
        if (key == null && ref != null) {
            return ref.getKey();
        }
        
        return key;
    }
    
    protected void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        if (title == null && ref != null) {
            return ref.getTitle();
        }
        
        return title;
    }
    
    protected void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        if (description == null && ref != null) {
            return ref.getDescription();
        }
        
        return description;
    }
    
    protected void setDescription(String description) {
        this.description = description;
    }
    
}
