
package com.googlecode.common.client.task;


/**
 * Controls running tasks.
 */
public final class TaskManager {
    
    public static final TaskManager INSTANCE = new TaskManager();

    private int                     taskCount;
    private TaskManagerUi           ui;
    
    
    private TaskManager() {
    }
    
    public void setUi(TaskManagerUi ui) {
        this.ui = ui;
    }
    
    public TaskManagerUi getUi() {
        if (ui == null) {
            throw new IllegalStateException("TaskManagerUi is not set");
        }
        
        return ui;
    }
    
//    public void execute(Runnable task) {
//        task.run();
//    }

    /**
     * Asynchronously executes the given task.
     */
    public void execute(AbstractTask task) {
        task.setTaskManager(this);
        task.run();
    }

    protected void onTaskStart(AbstractTask task) {
        if (taskCount++ == 0) {
            getUi().mask(task.getMessage());
        }
    }

    protected void onTaskFinish(AbstractTask task, int durationMillis) {
        if (--taskCount == 0) {
            getUi().unmask(task.getMessage() + "Done " 
                    + ((double)(durationMillis/10))/100 + " sec.");
        }
    }
    
}
