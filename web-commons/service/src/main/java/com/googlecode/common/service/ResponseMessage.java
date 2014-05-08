
package com.googlecode.common.service;

import java.util.Locale;


/**
 * Common interface for response messages.
 * 
 * <p>Provides needed information for server response.
 */
public interface ResponseMessage {
    
    /**
     * Returns server response status code.
     * @return server response status code
     */
    public int getStatus();
    
    /**
     * Returns response message for the given client locale.
     * 
     * @param locale    client locale for returned message
     * @return localized response message
     */
    public String getMessage(Locale locale);

}
