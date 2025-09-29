package com.enterprise.cleanqueen.dto.project;

import java.util.List;

import com.enterprise.cleanqueen.enums.TaskPriority;
import com.enterprise.cleanqueen.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Task update with optional subtasks")
public class TaskUpdateRequest {

    @Schema(description = "Task ID (null for new tasks)", example = "TASK123A")
    private String id; // null for new tasks, existing ID for updates

    @Schema(description = "Task name", example = "Updated Clean Office Floor", required = true)
    @NotBlank(message = "Task name is required")
    private String name;

    @Schema(description = "Task description", example = "Updated deep clean and mop entire office floor area")
    private String description;

    @Schema(description = "Task status", example = "IN_PROGRESS")
    private TaskStatus status;

    @Schema(description = "Task priority level", example = "HIGH")
    private TaskPriority priority;

    @Schema(description = "List of subtasks under this task")
    @Valid
    private List<TaskUpdateRequest> subtasks;

    // Constructors
    public TaskUpdateRequest() {
    }

    public TaskUpdateRequest(String id, String name, String description, TaskStatus status, TaskPriority priority, List<TaskUpdateRequest> subtasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.subtasks = subtasks;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public List<TaskUpdateRequest> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<TaskUpdateRequest> subtasks) {
        this.subtasks = subtasks;
    }
}
