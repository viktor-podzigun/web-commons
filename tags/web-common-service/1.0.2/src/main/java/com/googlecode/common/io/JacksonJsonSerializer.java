
package com.googlecode.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;


/**
 * Thread safe JSON implementation of {@link JsonSerializer} interface.
 */
public final class JacksonJsonSerializer implements JsonSerializer {
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	
	public JacksonJsonSerializer() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    
        // Date in ISO format "2012-04-07T17:00:00.000+0000" instead of 
        // 'long' format 
        objectMapper.configure(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        objectMapper.setVisibilityChecker(new VisibilityChecker.Std(
                Visibility.NONE,    // getter 
                Visibility.NONE,    // isGetter 
                Visibility.DEFAULT, // setter 
                Visibility.DEFAULT, // creator
                Visibility.ANY));   // field
    }
	
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
	
	@Override
	public <T> T deserialize(Class<T> clazz, Reader in) throws IOException {
		return objectMapper.readValue(in, clazz);
	}
	
	@Override
	public <T> T deserialize(Class<T> clazz, InputStream in) 
	        throws IOException {
		
	    return objectMapper.readValue(in, clazz);
	}
	
	@Override
	public void serialize(Object serialiedObj, Writer out) throws IOException {
		objectMapper.writeValue(out, serialiedObj);
	}
	
	@Override
	public void serialize(Object serialiedObj, OutputStream out) 
	        throws IOException {
		
	    objectMapper.writeValue(out, serialiedObj);
	}

	@Override
	public void serializePretty(Object data, Writer out) throws IOException {
	    objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, data);
	}
	
	@Override
	public String serializePretty(Object data) {
	    try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(data);
        
	    } catch (IOException x) {
	        throw new RuntimeException(x);
        }
	}

	@Override
	public <T> T convertValue(Object fromValue, Class<T> toValueType) {
	    return objectMapper.convertValue(fromValue, toValueType);
	}

}
