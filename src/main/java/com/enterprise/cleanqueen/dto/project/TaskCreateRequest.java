package com.enterprise.cleanqueen.dto.project;

import java.util.List;

import com.enterprise.cleanqueen.enums.TaskPriority;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Task creation with optional subtasks")
public class TaskCreateRequest {

    @Schema(description = "Task name", example = "Clean Office Floor", required = true)
    @NotBlank(message = "Task name is required")
    private String name;

    @Schema(description = "Task description", example = "Deep clean and mop entire office floor area")
    private String description;

    @Schema(description = "Task priority level", example = "HIGH")
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Schema(description = "List of subtasks under this task")
    @Valid
    private List<TaskCreateRequest> subtasks;

    // Constructors
    public TaskCreateRequest() {
    }

    public TaskCreateRequest(String name, String description, TaskPriority priority, List<TaskCreateRequest> subtasks) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.subtasks = subtasks;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public List<TaskCreateRequest> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<TaskCreateRequest> subtasks) {
        this.subtasks = subtasks;
    }
}
