
package com.googlecode.common.client.task;


/**
 * Contains base task functionality.
 */
public abstract class AbstractTask implements Runnable {

    private final String        message;
    
    private long                startTime;
    private boolean             isDone;
    private TaskManager         manager;
    
    
    public AbstractTask(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    TaskManager getTaskManager() {
        return manager;
    }
    
    void setTaskManager(TaskManager taskManager) {
        this.manager = taskManager;
    }
    
    protected abstract void runTask() throws Exception;
    
    protected void showError(String error) {
        manager.getUi().showError(error);
    }
    
    protected void showError(String error, Throwable exception) {
        manager.getUi().showError(error, exception);
    }
    
    protected void showError(String error, String details) {
        manager.getUi().showError(error, details);
    }
    
    @Override
    public void run() {
        try {
            onStart();
            
            runTask();
            
        } catch (Exception x) {
            onFinish();
            showError("runTask()", x);
        }
    }

    protected void onStart() {
        isDone = false;
        startTime = System.currentTimeMillis();
        manager.onTaskStart(this);
    }

    protected void onFinish() {
        if (!isDone) {
            isDone = true;
            manager.onTaskFinish(this, 
                    (int)(System.currentTimeMillis() - startTime));
        }
    }

}
