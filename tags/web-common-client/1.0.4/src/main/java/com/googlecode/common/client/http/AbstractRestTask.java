
package com.googlecode.common.client.http;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.googlecode.common.client.task.TaskWithResult;


/**
 * RestService {@link MethodCallback} task.
 */
public abstract class AbstractRestTask<T> extends TaskWithResult<T> 
        implements MethodCallback<T> {
    
    public AbstractRestTask(String message) {
        super(message);
    }
    
    @Override
    protected void processResult(T resp) {
        if (resp == null || isErrorResponse(resp)) {
            processFailureResponse(resp);
        } else {
            processSuccessResponse(resp);
        }
    }
    
    protected boolean isErrorResponse(T resp) {
        return false;
    }

    protected void showErrorResponse(T resp) {
        showError(resp.toString());
    }
    
    protected abstract void processSuccessResponse(T resp);
    
    protected void processFailureResponse(T resp) {
        if (resp == null) {
            showError("No response body");
            return;
        }
        
        showErrorResponse(resp);
    }

    @Override
    public void onSuccess(Method method, T response) {
        try {
            processResult(response);
        
        } catch (Exception x) {
            GWT.log("RestTask.onSuccess", x);
            showError("RestTask.onSuccess", x);
        
        } finally {
            onFinish();
        }
    }
    
    @Override
    public void onFailure(Method method, Throwable exception) {
        onFinish();
        
        Response response = method.getResponse();
        if (response.getStatusCode() == Response.SC_OK) {
            showError("Request failed", exception);
            return;
        }
        
        if (!handleErrorStatus(response)) {
            showErrorStatusMsg(response);
        }
    }

    protected boolean handleErrorStatus(Response response) {
        return RequestService.INSTANCE.handleErrorStatus(this, response);
    }
    
    protected void showErrorStatusMsg(Response response) {
        String text;
        final int statusCode = response.getStatusCode();
        switch (statusCode) {
        case Response.SC_UNAUTHORIZED:
            text = "User authentication failure";
            break;
        
        case Response.SC_FORBIDDEN:
            text = "Access denied for specified entity";
            break;
            
        default:
            text = response.getStatusText();
            if (text == null || text.isEmpty()) {
                text = "HTTP error status: " + statusCode;
            }
            break;
        }
        
        showError(text);
    }
    
}
