package com.enterprise.cleanqueen.dto.task;

import com.enterprise.cleanqueen.enums.TaskStatus;

public class TaskUpdateResult {
    
    private String taskId;
    private String taskName;
    private boolean success;
    private String message;
    private TaskStatus previousStatus;
    private TaskStatus newStatus;
    
    // Constructors
    public TaskUpdateResult() {}
    
    public TaskUpdateResult(String taskId, String taskName, boolean success, String message,
                           TaskStatus previousStatus, TaskStatus newStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.success = success;
        this.message = message;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }
    
    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public TaskStatus getPreviousStatus() {
        return previousStatus;
    }
    
    public void setPreviousStatus(TaskStatus previousStatus) {
        this.previousStatus = previousStatus;
    }
    
    public TaskStatus getNewStatus() {
        return newStatus;
    }
    
    public void setNewStatus(TaskStatus newStatus) {
        this.newStatus = newStatus;
    }
}