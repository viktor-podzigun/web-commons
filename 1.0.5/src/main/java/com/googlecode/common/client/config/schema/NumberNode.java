
package com.googlecode.common.client.config.schema;

import com.google.gwt.json.client.JSONValue;
import com.googlecode.common.client.util.JSONHelpers;
import com.googlecode.common.client.util.StringHelpers;


public class NumberNode extends PropertyNode<Number> {

    private Number      maximum;
    private Number      minimum;
    
    
    public NumberNode() {
        this(null);
    }
    
    protected NumberNode(NumberNode ref) {
        super(ref);
    }
    
    @Override
    public NumberNode asRef() {
        return new NumberNode(this);
    }
    
    @Override
    protected NumberNode getRef() {
        return (NumberNode)super.getRef();
    }
    
    @Override
    public JsonType getType() {
        return JsonType.NUMBER;
    }
    
    @Override
    public Number readValue(JSONValue json) {
        return JSONHelpers.toNumber(json);
    }

    public Number getMaximum() {
        NumberNode ref = getRef();
        if (maximum == null && ref != null) {
            return ref.getMaximum();
        }
        
        return maximum;
    }
    
    protected void setMaximum(Number maximum) {
        this.maximum = maximum;
    }
    
    public Number getMinimum() {
        NumberNode ref = getRef();
        if (minimum == null && ref != null) {
            return ref.getMinimum();
        }
        
        return minimum;
    }
    
    protected void setMinimum(Number minimum) {
        this.minimum = minimum;
    }
    
    @Override
    public Number parseValue(String val) throws PropertyValidationException {
        if (StringHelpers.isNullOrEmpty(val)) {
            throw new PropertyValidationException("Value is null or empty");
        }
        
        try {
            double numVal = Double.parseDouble(val);
            
            Number minimum = getMinimum();
            if (minimum != null && numVal < minimum.doubleValue()) {
                throw new PropertyValidationException(
                    "Value is less than minimum");
            }
            
            Number maximum = getMaximum();
            if (maximum != null && numVal > maximum.doubleValue()) {
                throw new PropertyValidationException(
                    "Value is greater than maximum");
            }
            
            return numVal;
            
        } catch (NumberFormatException x) {
            throw new PropertyValidationException(
                    "Value is not a number");
        }
    }

}
