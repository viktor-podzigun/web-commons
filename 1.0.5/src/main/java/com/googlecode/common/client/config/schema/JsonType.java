
package com.googlecode.common.client.config.schema;


public enum JsonType {

    /** Array nodes */
    ARRAY("array"),
    
    /** Boolean nodes */
    BOOLEAN("boolean"),
    
    /** Integer nodes */
    INTEGER("integer"),
    
    /** Null nodes */
    NULL("null"),
    
    /** Number nodes (ie, decimal numbers) */
    NUMBER("number"),
    
    /** Object nodes */
    OBJECT("object"),
    
    /** String nodes */
    STRING("string"),
    
    ;

    
    /**
     * The name for this type, as encountered in a JSON schema
     */
    private final String name;

    
    JsonType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Given a type name, return the corresponding node type
     *
     * @param name  the type name
     * @return      the node type, or <code>NodeType.NULL</code> if not found
     */
    public static JsonType fromName(String name) {
        if (name == null) {
            return NULL;
        }
        
        for (JsonType val : values()) {
            if (val.name.equals(name)) {
                return val;
            }
        }
        
        return NULL;
    }

}
