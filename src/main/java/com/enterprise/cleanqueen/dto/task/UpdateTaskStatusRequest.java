package com.enterprise.cleanqueen.dto.task;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class UpdateTaskStatusRequest {
    
    @NotEmpty(message = "Tasks list cannot be empty")
    @Valid
    private List<TaskStatusUpdate> tasks;
    
    // Constructors
    public UpdateTaskStatusRequest() {}
    
    public UpdateTaskStatusRequest(List<TaskStatusUpdate> tasks) {
        this.tasks = tasks;
    }
    
    // Getters and Setters
    public List<TaskStatusUpdate> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<TaskStatusUpdate> tasks) {
        this.tasks = tasks;
    }
}