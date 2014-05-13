
package com.googlecode.common.http;

import com.googlecode.common.io.XmlSerializer;


/**
 * Helper class for running XML server requests
 */
public class XmlRequestClient extends AbstractRequestClient {

    
    public XmlRequestClient(HttpConnector httpService, 
            XmlSerializer serializer) {
        
        super(httpService, serializer, "application/xml");
    }

}
