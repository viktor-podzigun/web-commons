
package com.googlecode.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


/**
 * Contains basic serialization/deserialization methods.
 */
public interface BaseSerializer {

    /**
     * Deserializes specified object from buffered reader.
     * 
     * @param clazz    objects class type to deserialize
     * @param in       input reader to read object from
     * @return         deserialized object
     * 
     * @throws IOException if I/O error occurs
     */
	public <T> T deserialize(Class<T> clazz, Reader in) throws IOException;
	
    /**
     * Deserializes specified object from specified input stream.
     * 
     * @param clazz    objects class type to deserialize
     * @param in       input stream to read object from
     * @return         deserialized object
     * 
     * @throws IOException if I/O error occurs
     */
	public <T> T deserialize(Class<T> clazz, InputStream in) throws IOException;
	
	/**
	 * Serializes the specified object into the specified writer.
	 * 
	 * @param data     data object to serialize 
	 * @param out      writer to serialize to
	 * 
	 * @throws IOException if I/O error occurs
	 */
	public void serialize(Object data, Writer out) throws IOException;
	
	/**
	 * Serializes the specified object into the specified output stream.
	 * 
	 * @param data     data object to serialize
	 * @param out      output stream to serialize to
	 * 
	 * @throws IOException if I/O error occurs
	 */
	public void serialize(Object data, OutputStream out) throws IOException;

}
