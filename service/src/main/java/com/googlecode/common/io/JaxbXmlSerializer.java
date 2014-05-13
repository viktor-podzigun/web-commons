
package com.googlecode.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * Thread safe JAXB XML implementation of XmlSerializer interface.
 * 
 * @see XmlSerializer
 */
public final class JaxbXmlSerializer implements XmlSerializer {

    private final JAXBContext context;
    
    
    public JaxbXmlSerializer(String contextPath) {
        try {
            context = JAXBContext.newInstance(contextPath);
        
        } catch (JAXBException x) {
            throw new RuntimeException(x);
        }
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialize(Class<T> clazz, Reader in) throws IOException {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(in);
            if (obj instanceof JAXBElement) {
                obj = ((JAXBElement)obj).getValue();
            }
            if (!clazz.isInstance(obj)) {
                throw new IOException(clazz + " not assignable from " 
                        + obj.getClass());
            }

            T result = (T)obj;
            return result;
            
        } catch (JAXBException x) {
            throw new IOException(x);
        }
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialize(Class<T> clazz, InputStream in) throws IOException {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(in);
            if (obj instanceof JAXBElement) {
                obj = ((JAXBElement)obj).getValue();
            }
            if (!clazz.isInstance(obj)) {
                throw new IOException(clazz + " not assignable from " 
                        + obj.getClass());
            }
            
            T result = (T)obj;
            return result;
            
        } catch (JAXBException x) {
            throw new IOException(x);
        }
    }
    
    @Override
    public void serialize(Object data, Writer out) throws IOException {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(data, out);
        
        } catch (JAXBException x) {
            throw new IOException(x);
        }
    }
    
    @Override
    public void serialize(Object data, OutputStream out) throws IOException {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(data, out);
        
        } catch (JAXBException x) {
            throw new IOException(x);
        }
    }
}
