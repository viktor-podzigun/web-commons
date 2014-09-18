
package com.googlecode.common.client.config.schema;

import com.google.gwt.json.client.JSONValue;
import com.googlecode.common.client.util.JSONHelpers;
import com.googlecode.common.client.util.StringHelpers;


public final class BooleanNode extends PropertyNode<Boolean> {

    
    public BooleanNode() {
        this(null);
    }
    
    private BooleanNode(BooleanNode ref) {
        super(ref);
    }
    
    @Override
    public BooleanNode asRef() {
        return new BooleanNode(this);
    }
    
    @Override
    public JsonType getType() {
        return JsonType.BOOLEAN;
    }
    
    @Override
    public Boolean readValue(JSONValue json) {
        return JSONHelpers.toBoolean(json);
    }

    @Override
    public Boolean parseValue(String val) throws PropertyValidationException {
        if (StringHelpers.isNullOrEmpty(val)) {
            throw new PropertyValidationException("Value is null or empty");
        }
        
        if ("false".equals(val)) {
            return Boolean.FALSE;
        }
        
        if ("true".equals(val)) {
            return Boolean.TRUE;
        }
        
        throw new PropertyValidationException(
                "Value should be either true or false");
    }

}
