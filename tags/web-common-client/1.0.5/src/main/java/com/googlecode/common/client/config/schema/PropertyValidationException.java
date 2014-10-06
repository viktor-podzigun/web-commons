
package com.googlecode.common.client.config.schema;


public final class PropertyValidationException extends Exception {

    private static final long serialVersionUID = 9195522519582795517L;

    
    public PropertyValidationException(String message) {
        super(message);
        
        if (message == null) {
            throw new NullPointerException("message");
        }
    }

}
