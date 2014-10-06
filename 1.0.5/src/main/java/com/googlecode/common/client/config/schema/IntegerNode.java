
package com.googlecode.common.client.config.schema;

import com.google.gwt.json.client.JSONValue;
import com.googlecode.common.client.util.JSONHelpers;
import com.googlecode.common.client.util.StringHelpers;


public final class IntegerNode extends NumberNode {

    
    public IntegerNode() {
        this(null);
    }
    
    private IntegerNode(IntegerNode ref) {
        super(ref);
    }
    
    @Override
    public IntegerNode asRef() {
        return new IntegerNode(this);
    }
    
    @Override
    public JsonType getType() {
        return JsonType.INTEGER;
    }

    @Override
    public Integer readValue(JSONValue json) {
        return JSONHelpers.toInteger(json);
    }

    @Override
    public Integer parseValue(String val) throws PropertyValidationException {
        if (StringHelpers.isNullOrEmpty(val)) {
            throw new PropertyValidationException("Value is null or empty");
        }
        
        try {
            int intVal = Integer.parseInt(val);
            
            Number minimum = getMinimum();
            if (minimum != null && intVal < minimum.intValue()) {
                throw new PropertyValidationException(
                    "Value is less than minimum");
            }
            
            Number maximum = getMaximum();
            if (maximum != null && intVal > maximum.intValue()) {
                throw new PropertyValidationException(
                    "Value is greater than maximum");
            }
            
            return intVal;
            
        } catch (NumberFormatException x) {
            throw new PropertyValidationException(
                    "Value is not an integer");
        }
    }

}
