
package com.googlecode.common.protocol;


/**
 * Base response for all server requests.
 * 
 * <p>Status codes from <code>0</code> to <code>100</code> considered as 
 * OK statuses and all other are error statuses.
 */
public class BaseResponse {

    public static final int             OK_STATUS = 0;
    public static final BaseResponse    OK        = new ReadOnlyOkResponse();
    
	private int        status;
	private String     message;
	private String     error;
	
	
	/**
	 * Creates response with OK status and no message.
	 */
	protected BaseResponse() {
		this(OK_STATUS, null);
	}
	
	/**
	 * Creates base response with status and no message.
	 * 
	 * @param status   response status
	 */
	public BaseResponse(int status) {
		this(status, null);
	}
	
	/**
	 * Creates base response with status and message.
	 * 
	 * @param status   response status
	 * @param message  message describing error if any
	 */
	public BaseResponse(int status, String message) {
		this.status   = status;
		this.message  = message;
	}
	
    /**
     * Checks whether the status of this response is error status.
     * 
     * <p>Status codes from <code>0</code> to <code>100</code> considered as 
     * OK statuses and all other are error statuses.
     * 
     * @return <code>true</code> if status of this response is error status, 
     *         and <code>false</code> if it's OK status
     */
    public static boolean isErrorStatus(int status) {
        return (status > 100);
    }
    
    /**
     * Copies the given source response to the given destination response 
     * object.
     * 
     * @param dst   destination response
     * @param src   source response
     * @return      destination response object
     */
    public static <T extends BaseResponse> T copy(T dst, BaseResponse src) {
        dst.setStatus(src.getStatus());
        dst.setMessage(src.getMessage());
        dst.setError(src.getError());
        return dst;
    }
    
	/**
	 * Returns request completion status.
	 * 
	 * @return the status of request completion
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets request completion status.
	 * 
	 * @param status   the status of request completion
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Sets optional message that helps to identify the problem if any.
	 * 
	 * @return the message message that helps to identify the problem if any
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns optional message that helps to identify the problem if any.
	 * 
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return getClass().getName() + ":\nstatus: " + status
                + (message != null ? ", message: " + message : "") 
                + (error != null ? "\nerror: " + error : "");
    }
    
    
    /**
     * Read-only OK response.
     */
    private static final class ReadOnlyOkResponse extends BaseResponse {

        @Override
        public void setStatus(int status) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setMessage(String message) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setError(String error) {
            throw new UnsupportedOperationException();
        }
    }
	
}
