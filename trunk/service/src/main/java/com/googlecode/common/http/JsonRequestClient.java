
package com.googlecode.common.http;

import com.googlecode.common.io.JacksonJsonSerializer;
import com.googlecode.common.io.JsonSerializer;


/**
 * Helper class for running JSON server requests
 */
public class JsonRequestClient extends AbstractRequestClient {

    
    /**
     * Creates JsonRequestClient
     */
    public JsonRequestClient(HttpConnector httpService) {
        this(httpService, new JacksonJsonSerializer());
    }
    
    public JsonRequestClient(HttpConnector httpService, 
            JsonSerializer serializer) {
        
        super(httpService, serializer, "application/json");
    }
    
}
