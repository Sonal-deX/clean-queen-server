package com.enterprise.cleanqueen.dto.task;

import java.time.LocalDateTime;

import com.enterprise.cleanqueen.enums.TaskStatus;

public class UpdateTaskStatusResponse {
    
    private boolean success;
    private String message;
    private String taskId;
    private String taskName;
    private TaskStatus previousStatus;
    private TaskStatus newStatus;
    private String updatedBy;
    private LocalDateTime timestamp;
    
    // Constructors
    public UpdateTaskStatusResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public UpdateTaskStatusResponse(boolean success, String message, String taskId, String taskName,
                                  TaskStatus previousStatus, TaskStatus newStatus, String updatedBy) {
        this();
        this.success = success;
        this.message = message;
        this.taskId = taskId;
        this.taskName = taskName;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.updatedBy = updatedBy;
    }
    
    // Getters and Setters
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
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}