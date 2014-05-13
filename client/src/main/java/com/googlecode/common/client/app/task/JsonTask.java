
package com.googlecode.common.client.app.task;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.json.client.JSONValue;
import com.googlecode.common.client.http.AbstractRestTask;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Task for handling generic JSON responses.
 */
public abstract class JsonTask<T extends BaseResponse> extends 
        AbstractRestTask<JSONValue> implements JsonCallback {

    private final JsonEncoderDecoder<T> decoder;
    
    private T    resp;
    
    
    public JsonTask(String message, JsonEncoderDecoder<T> decoder) {
        super(message);
        
        this.decoder = decoder;
    }

    protected T decode(JSONValue val) {
        if (resp == null) {
            resp = decoder.decode(val);
        }
        
        return resp;
    }
    
    @Override
    protected boolean isErrorResponse(JSONValue val) {
        return BaseResponse.isErrorStatus(decode(val).getStatus());
    }
    
    @Override
    protected void showErrorResponse(JSONValue val) {
        BaseResponse resp = decode(val);
        showError(resp.getMessage(), resp.toString());
    }
    
    @Override
    protected void processSuccessResponse(JSONValue val) {
        processSuccessResponse(decode(val));
    }
    
    protected abstract void processSuccessResponse(T resp);

}
