
package com.googlecode.common.client.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;


public abstract class AbstractTextTask extends AbstractTask implements 
        ResourceCallback<TextResource> {
        
    private final ExternalTextResource  resource;
    
    
    public AbstractTextTask(String message, ExternalTextResource res) {
        super(message);
        
        this.resource = res;
    }
    
    @Override
    protected void runTask() throws Exception {
        resource.getText(this);
    }
    
    @Override
    public void onError(ResourceException x) {
        onFinish();
        
        showError("Failed to load resource: " + resource.getName(), x);
    }
    
    @Override
    public void onSuccess(TextResource resource) {
        try {
            processResult(resource.getText());
        
        } catch (Exception x) {
            GWT.log("TextTask.onSuccess", x);
            showError("TextTask.onSuccess", x);
        
        } finally {
            onFinish();
        }
    }
    
    public abstract void processResult(String text);
    
}
