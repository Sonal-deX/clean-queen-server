package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;

public class DeleteTaskResponse {
    
    private boolean success;
    private String message;
    private String taskId;
    private String taskName;
    private LocalDateTime timestamp;
    
    // Constructors
    public DeleteTaskResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public DeleteTaskResponse(boolean success, String message, String taskId, String taskName) {
        this();
        this.success = success;
        this.message = message;
        this.taskId = taskId;
        this.taskName = taskName;
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
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}