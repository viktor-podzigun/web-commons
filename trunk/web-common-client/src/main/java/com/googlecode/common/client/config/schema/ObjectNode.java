
package com.googlecode.common.client.config.schema;

import java.util.Collections;
import java.util.List;


public final class ObjectNode extends ComplexNode {

    private List<PropertyNode<?>>   properties;
    private List<ComplexNode>       nodes;
    
    
    public ObjectNode() {
        this(null);
    }
    
    private ObjectNode(ObjectNode ref) {
        super(ref);
    }
    
    @Override
    public JsonType getType() {
        return JsonType.OBJECT;
    }
    
    @Override
    public ObjectNode asRef() {
        return new ObjectNode(this);
    }
    
    @Override
    protected ObjectNode getRef() {
        return (ObjectNode)super.getRef();
    }
    
    public List<PropertyNode<?>> getProperties() {
        if (properties == null) {
            ObjectNode ref = getRef();
            if (ref != null) {
                return ref.getProperties();
            }
            
            return Collections.emptyList();
        }
        
        return properties;
    }
    
    protected void setProperties(List<PropertyNode<?>> properties) {
        this.properties = properties;
    }
    
    public List<ComplexNode> getNodes() {
        if (nodes == null) {
            ObjectNode ref = getRef();
            if (ref != null) {
                return ref.getNodes();
            }
            
            return Collections.emptyList();
        }
        
        return nodes;
    }
    
    protected void setNodes(List<ComplexNode> nodes) {
        this.nodes = nodes;
    }
    
}
