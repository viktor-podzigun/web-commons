
package com.googlecode.common.service.ex;

import com.googlecode.common.service.ResponseMessage;


/**
 * Base exception for all operations on any service. 
 */
public class OperationFailedException extends RuntimeException {

	private static final long serialVersionUID = 3799197632013182143L;
	
	private final ResponseMessage  respMsg;

	
    public OperationFailedException(ResponseMessage respMsg, String error) {
        super(error);
        
        if (respMsg == null) {
            throw new NullPointerException("respMsg");
        }
        
        this.respMsg = respMsg;
    }

    public OperationFailedException(ResponseMessage respMsg, String error, 
            Throwable cause) {
        
        super(error, cause);
        
        if (respMsg == null) {
            throw new NullPointerException("respMsg");
        }
        
        this.respMsg = respMsg;
    }

    /**
     * Returns response message.
     * 
     * <p>Always returns non null object. 
     * 
     * @return response message
     */
    public ResponseMessage getResponseMessage() {
        return respMsg;
    }

}
