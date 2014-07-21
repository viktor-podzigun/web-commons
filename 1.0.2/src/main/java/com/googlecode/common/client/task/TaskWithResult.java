
package com.googlecode.common.client.task;


/**
 * Used for tasks with results.
 */
public abstract class TaskWithResult<T> extends AbstractTask {

    
    public TaskWithResult(String message) {
        super(message);
    }
    
    /**
     * Called from event thread
     * 
     * @param taskResult  task result object
     */
    protected abstract void processResult(T taskResult);

}
