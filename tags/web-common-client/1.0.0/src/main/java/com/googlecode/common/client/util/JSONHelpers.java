
package com.googlecode.common.client.util;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


/**
 * Contains helper methods for working with {@link JSONValue}.
 */
public final class JSONHelpers {

    
    private JSONHelpers() {
    }
    
    /**
     * Converts the given JSON value to JSON object.
     * 
     * @param json  JSON value
     * @return      JSON object or <code>null</code> if it is not object
     */
    public static JSONObject toObject(JSONValue json) {
        return (json != null ? json.isObject() : null);
    }
    
    /**
     * Converts the given JSON value to string.
     * 
     * @param json  JSON value
     * @return      string or <code>null</code> if it is not string
     */
    public static String toString(JSONValue json) {
        JSONString obj = null;
        if (json != null) {
            obj = json.isString();
        }
        
        return (obj != null ? obj.stringValue() : null);
    }
    
    /**
     * Converts the given JSON value to JSON number.
     * 
     * @param json  JSON value
     * @return      JSON number or <code>null</code> if it is not number
     */
    private static JSONNumber toNum(JSONValue json) {
        return (json != null ? json.isNumber() : null);
    }
    
    /**
     * Converts the given JSON value to double.
     * 
     * @param json  JSON value
     * @return      double or <code>null</code> if it is not number
     */
    public static Double toNumber(JSONValue json) {
        JSONNumber num = toNum(json);
        return (num != null ? num.doubleValue() : null);
    }
    
    /**
     * Converts the given JSON value to integer.
     * 
     * @param json  JSON value
     * @return      integer or <code>null</code> if it is not integer
     */
    public static Integer toInteger(JSONValue json) {
        JSONNumber num = toNum(json);
        return (num != null ? (int)num.doubleValue() : null);
    }
    
    /**
     * Converts the given JSON value to JSON array.
     * 
     * @param json  JSON value
     * @return      JSON array or <code>null</code> if it is not array
     */
    public static JSONArray toArray(JSONValue json) {
        return (json != null ? json.isArray() : null);
    }
    
    /**
     * Converts the given JSON value to boolean.
     * 
     * @param json  JSON value
     * @return      boolean or <code>null</code> if it is not boolean
     */
    public static Boolean toBoolean(JSONValue json) {
        JSONBoolean bool = (json != null ? json.isBoolean() : null);
        return (bool != null ? bool.booleanValue() : null);
    }
    
    /**
     * Returns the value for the given key as JSON object.
     * 
     * @param json  JSON object
     * @param key   key name
     * @return      the value for the given key as JSON object 
     *              or <code>null</code> if it is not found or not an object
     * 
     * @throws NullPointerException 
     *              if the given JSON value or key is <code>null</code>
     */
    public static JSONObject getObject(JSONObject json, String key) {
        JSONValue val = json.get(key);
        return (val != null ? toObject(val) : null);
    }

    /**
     * Returns the value for the given key as string.
     * 
     * @param json  JSON object
     * @param key   key name
     * @return      the value for the given key as string
     *              or <code>null</code> if it is not found or not a string
     * 
     * @throws NullPointerException 
     *              if the given JSON value or key is <code>null</code>
     */
    public static String getString(JSONObject json, String key) {
        return toString(json.get(key));
    }

    /**
     * Returns the value for the given key as number.
     * 
     * @param json  JSON object
     * @param key   key name
     * @return      the value for the given key as number
     *              or <code>null</code> if it is not found or not a number
     * 
     * @throws NullPointerException 
     *              if the given JSON value or key is <code>null</code>
     */
    public static Double getNumber(JSONObject json, String key) {
        return toNumber(json.get(key));
    }

    /**
     * Returns the value for the given key as integer.
     * 
     * @param json  JSON object
     * @param key   key name
     * @return      the value for the given key as integer
     *              or <code>0</code> if it is not found or not an integer
     * 
     * @throws NullPointerException 
     *              if the given JSON value or key is <code>null</code>
     */
    public static Integer getInteger(JSONObject json, String key) {
        return toInteger(json.get(key));
    }

    /**
     * Returns the value for the given key as array.
     * 
     * @param json  JSON object
     * @param key   key name
     * @return      the value for the given key as array
     *              or <code>null</code> if it is not found or not an array
     * 
     * @throws NullPointerException 
     *              if the given JSON value or key is <code>null</code>
     */
    public static JSONArray getArray(JSONObject json, String key) {
        return toArray(json.get(key));
    }

    /**
     * Returns the value for the given key as boolean.
     * 
     * @param json  JSON object
     * @param key   key name
     * @return      the value for the given key as boolean
     * 
     * @throws NullPointerException 
     *              if the given JSON value or key is <code>null</code>
     */
    public static Boolean getBoolean(JSONObject json, String key) {
        return toBoolean(json.get(key));
    }

}
