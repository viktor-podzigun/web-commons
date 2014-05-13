
package com.googlecode.common.client.http;

import com.google.gwt.http.client.Response;
import com.googlecode.common.client.task.AbstractTask;


/**
 * Global error handler for request service.
 */
public interface RequestErrorHandler {

    public boolean handleErrorStatus(AbstractTask task, Response response);
    
}
