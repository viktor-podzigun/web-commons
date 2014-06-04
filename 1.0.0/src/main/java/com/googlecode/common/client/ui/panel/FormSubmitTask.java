
package com.googlecode.common.client.ui.panel;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.googlecode.common.client.task.AbstractTask;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Abstract task for submitting form data.
 */
public abstract class FormSubmitTask<T extends BaseResponse> extends 
        AbstractTask implements SubmitHandler, SubmitCompleteHandler {
    
    
    private final JsonEncoderDecoder<T> decoder;
    
    
    public FormSubmitTask(String message, JsonEncoderDecoder<T> decoder) {
        super(message);
        
        this.decoder = decoder;
    }

    @Override
    protected void runTask() throws Exception {
    }

    @Override
    public void onSubmit(SubmitEvent event) {
        TaskManager.INSTANCE.execute(this);
    }
    
    private T parseResponse(String results) {
        if (results == null) {
            return null;
        }
        
        if (results.startsWith("<pre>")) {
            results = results.substring("<pre>".length(), 
                    results.length() - "</pre>".length());
        }
        
        return decoder.decode(JSONParser.parseLenient(results));
    }
    
    @Override
    public void onSubmitComplete(final SubmitCompleteEvent event) {
        onFinish();
        
        try {
            T response = parseResponse(event.getResults());
            if (response == null) {
                ErrorPanel.show("No response body");
                return;
            }
            
            if (BaseResponse.isErrorStatus(response.getStatus())) {
                ErrorPanel.showDetailed(response.getMessage(), 
                        response.toString());
                return;
            }
            
            processResponse(response);
            
        } catch (Exception x) {
            ErrorPanel.showError("Failed to process response", x);
        }
    }
    
    protected abstract void processResponse(T response);

}
