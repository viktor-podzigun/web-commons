
package com.googlecode.common.service.ex;

import com.googlecode.common.service.ResponseMessage;


/**
 * Exception thrown by service when input validation failed.
 */
public final class ValidationFailedException extends OperationFailedException {

	private static final long serialVersionUID = 3799197632013182143L;

	
    public ValidationFailedException(ResponseMessage respMsg, String message, 
            Throwable cause) {
        
        super(respMsg, message, cause);
    }

    public ValidationFailedException(ResponseMessage respMsg, String message) {
        super(respMsg, message);
    }

}
