
package com.googlecode.common.client.task;


/**
 * Global error handler for task manager.
 */
public interface TaskManagerUi {
    
    public void showError(String error);
    
    public void showError(String error, Throwable exception);
    
    public void showError(String error, String details);

    public void mask(String text);
    
    public void unmask(String text);
    
}
