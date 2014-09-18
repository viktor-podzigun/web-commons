
package com.googlecode.common.client.config.schema;

import com.google.gwt.json.client.JSONValue;
import com.googlecode.common.client.util.JSONHelpers;
import com.googlecode.common.client.util.StringHelpers;


public final class StringNode extends PropertyNode<String> {

    
    public StringNode() {
        this(null);
    }
    
    private StringNode(StringNode ref) {
        super(ref);
    }
    
    @Override
    public StringNode asRef() {
        return new StringNode(this);
    }
    
    @Override
    public JsonType getType() {
        return JsonType.STRING;
    }

    @Override
    public String readValue(JSONValue json) {
        return JSONHelpers.toString(json);
    }

    @Override
    public String parseValue(String val) throws PropertyValidationException {
        if (StringHelpers.isNullOrEmpty(val)) {
            throw new PropertyValidationException("Value is null or empty");
        }
        
        return val;
    }

}
