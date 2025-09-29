package com.enterprise.cleanqueen.dto.task;

import com.enterprise.cleanqueen.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskStatusUpdate {
    
    @NotBlank(message = "Task ID is required")
    private String taskId;
    
    @NotNull(message = "Status is required")
    private TaskStatus status;
    
    private String remarks; // Optional remarks for status update
    
    // Constructors
    public TaskStatusUpdate() {}
    
    public TaskStatusUpdate(String taskId, TaskStatus status, String remarks) {
        this.taskId = taskId;
        this.status = status;
        this.remarks = remarks;
    }
    
    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}