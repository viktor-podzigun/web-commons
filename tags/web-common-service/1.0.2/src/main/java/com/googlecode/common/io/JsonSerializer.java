
package com.googlecode.common.io;

import java.io.IOException;
import java.io.Writer;


/**
 * JSON serializer service.
 */
public interface JsonSerializer extends BaseSerializer {

    /**
     * Serializes the specified object into the specified writer using 
     * pretty formatting.
     * 
     * @param data  data object to serialize
     * @param out   writer to serialize to
     * 
     * @throws IOException if I/O error occurs
     */
    public void serializePretty(Object data, Writer out) throws IOException;
    
    /**
     * Serializes the specified object to JSON string using pretty formatting.
     * 
     * <p>Functionally equivalent to calling 
     * {@link #serializePretty(Object, Writer)} with {@link java.io.StringWriter}
     * and constructing String, but more efficient.
     * 
     * @param data  data object to serialize
     * @return      serialized data as JSON string
     */
    public String serializePretty(Object data);
    
    /**
     * Convenience method for doing two-step conversion from given value, into
     * instance of given value type. This is functionality equivalent to first
     * serializing given value into JSON, then binding JSON data into value
     * of given type, but may be executed without fully serializing into
     * JSON. Same converters (serializers, deserializers) will be used as for
     * data binding, meaning same object mapper configuration works.
     * 
     * @param fromValue     source value object
     * @param toValueType   destination value type
     * @return              converted instance of destination type
     */
    public <T> T convertValue(Object fromValue, Class<T> toValueType);
    
}
