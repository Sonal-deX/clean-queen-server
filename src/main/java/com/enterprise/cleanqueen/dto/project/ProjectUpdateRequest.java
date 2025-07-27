package com.enterprise.cleanqueen.dto.project;

import java.util.List;

import com.enterprise.cleanqueen.enums.ProjectStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Update project with hierarchical tasks")
public class ProjectUpdateRequest {

    @Schema(description = "Updated project name", example = "Updated Faculty Building Cleaning", required = true)
    @NotBlank(message = "Project name is required")
    private String name;

    @Schema(description = "Updated project description", example = "Updated complete cleaning of university faculty building")
    private String description;

    @Schema(description = "Updated project status", example = "IN_PROGRESS")
    private ProjectStatus status;

    @Schema(description = "Updated list of root-level tasks for this project", required = true)
    @Valid
    private List<TaskUpdateRequest> tasks;

    // Constructors
    public ProjectUpdateRequest() {
    }

    public ProjectUpdateRequest(String name, String description, ProjectStatus status, List<TaskUpdateRequest> tasks) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.tasks = tasks;
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

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<TaskUpdateRequest> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskUpdateRequest> tasks) {
        this.tasks = tasks;
    }
}
