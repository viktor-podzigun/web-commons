
package com.googlecode.common.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import com.googlecode.common.io.JacksonJsonSerializer;
import com.googlecode.common.io.JsonSerializer;


/**
 * Customizes JSON objects serialization/deserialization by setting custom 
 * Jackson ObjectMapper.
 * 
 * <p>Spring will set the adapter field first, then run the init() method. 
 * It will 'dig' at the existing beans and find the Jackson converter, 
 * then replace the default object mapper with the custom object mapper. 
 * 
 * <p>See <a 
 * href='http://magicmonster.com/kb/prg/java/spring/webmvc/jackson_custom.html'>
 *    How to customise the Jackson JSON mapper in Spring Web MVC</a>
 */
@Service("jsonSerializer")
public class JsonSerializerImpl implements JsonSerializer {

    private RequestMappingHandlerAdapter    adapter;
    private JacksonJsonSerializer           serializer;
    

    @PostConstruct
    public void init() {
        serializer = new JacksonJsonSerializer();
        
        for (HttpMessageConverter<?> conv : adapter.getMessageConverters()) {
            if (conv instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter m = 
                    (MappingJackson2HttpMessageConverter) conv;
                
                // replace the default object mapper with the common one
                m.setObjectMapper(serializer.getObjectMapper());
            }
        }
    }

    // this will exist due to the <mvc:annotation-driven/> bean
    @Autowired
    public void setRequestMappingHandlerAdapter(
            RequestMappingHandlerAdapter adapter) {
        
        this.adapter  = adapter;
    }

    @Override
    public <T> T deserialize(Class<T> clazz, Reader in) throws IOException {
        return serializer.deserialize(clazz, in);
    }
    
    @Override
    public <T> T deserialize(Class<T> clazz, InputStream in) 
            throws IOException {
        
        return serializer.deserialize(clazz, in);
    }
    
    @Override
    public void serialize(Object serialiedObj, Writer out) throws IOException {
        serializer.serialize(serialiedObj, out);
    }
    
    @Override
    public void serialize(Object serialiedObj, OutputStream out) 
            throws IOException {
        
        serializer.serialize(serialiedObj, out);
    }
    
    @Override
    public void serializePretty(Object data, Writer out) throws IOException {
        serializer.serializePretty(data, out);
    }
}
