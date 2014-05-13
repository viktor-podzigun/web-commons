
package com.googlecode.common.protocol;


/**
 * Generic response for retrieving data of any type.
 */
public class DataResponse<T> extends BaseResponse {

    private T   data;
    
    
    public DataResponse() {
    }
    
    public DataResponse(int status, String message) {
        super(status, message);
    }
    
    public DataResponse(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
