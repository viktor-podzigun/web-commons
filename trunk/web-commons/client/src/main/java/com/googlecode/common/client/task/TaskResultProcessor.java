
package com.googlecode.common.client.task;


/**
 * Generic callback-interface for processing task's results.
 */
public interface TaskResultProcessor<T> {

    /**
     * Callback-method for processing task's results.
     * 
     * @param result    task's result object, can be <code>null</code> for 
     *                  failed results
     */
    public void processTaskResult(T result);
    
}
