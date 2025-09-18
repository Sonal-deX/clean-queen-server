package com.enterprise.cleanqueen.dto.project;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Create project with hierarchical tasks")
public class ProjectCreateRequest {

    @Schema(description = "Project name", example = "Faculty Building Cleaning", required = true)
    @NotBlank(message = "Project name is required")
    private String name;

    @Schema(description = "Project description", example = "Complete cleaning of university faculty building")
    private String description;

    @Schema(description = "List of root-level tasks for this project", required = true)
    @Valid
    private List<TaskCreateRequest> tasks;

    // Constructors
    public ProjectCreateRequest() {
    }

    public ProjectCreateRequest(String name, String description, List<TaskCreateRequest> tasks) {
        this.name = name;
        this.description = description;
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

    public List<TaskCreateRequest> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskCreateRequest> tasks) {
        this.tasks = tasks;
    }
}
