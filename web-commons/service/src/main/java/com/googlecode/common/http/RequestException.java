
package com.googlecode.common.http;

import java.io.IOException;


/**
 * Generic request exception.
 */
public class RequestException extends IOException {

    private static final long serialVersionUID = -7499717185869642236L;

    private final int       status;
    private final String    statusText;
    private final String    response;
    
    
    public RequestException(int status, String statusText, String response) {
        super(statusText != null ? statusText : response);
        
        this.status     = status;
        this.statusText = statusText;
        this.response   = response;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getStatusText() {
        return statusText;
    }
    
    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{status: " + status
                + (statusText != null ? ", statusText: " + statusText : "")
                + (response != null ? ", response: " + response : "") 
                + "}";
    }
    
}
