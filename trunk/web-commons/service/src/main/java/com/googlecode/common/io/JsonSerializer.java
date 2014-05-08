
package com.googlecode.common.io;

import java.io.IOException;
import java.io.Writer;


/**
 * JSON serializer tagging interface.
 */
public interface JsonSerializer extends BaseSerializer {

    /**
     * Serializes the specified object into the specified writer using 
     * pretty formatting.
     * 
     * @param data     data object to serialize 
     * @param out      writer to serialize to
     * 
     * @throws IOException if I/O error occurs
     */
    public void serializePretty(Object data, Writer out) throws IOException;
    
}
