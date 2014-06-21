
package com.googlecode.common.client.app.task;

import com.googlecode.common.client.http.AbstractRestTask;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Abstract server request task.
 */
public abstract class RequestTask<T extends BaseResponse> extends 
        AbstractRestTask<T> {
    
    public RequestTask(String message) {
        super(message);
    }

    @Override
    protected boolean isErrorResponse(T resp) {
        return BaseResponse.isErrorStatus(resp.getStatus());
    }
    
    @Override
    protected void showErrorResponse(T resp) {
        showError(resp.getMessage(), resp.toString());
    }
    
}
