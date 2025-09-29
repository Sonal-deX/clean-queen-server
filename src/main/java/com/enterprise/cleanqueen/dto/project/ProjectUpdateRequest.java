package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDate;
import java.util.List;

import com.enterprise.cleanqueen.enums.ProjectStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    @Schema(description = "Updated project due date", example = "2024-01-15")
    private LocalDate dueDate;

    @Schema(description = "Updated project address", example = "123 University Ave, Faculty Building, Room 101")
    private String address;

    @Schema(description = "Updated number of cleaners required", example = "3")
    @Min(value = 1, message = "Number of cleaners must be at least 1")
    private Integer noOfCleaners;

    @Schema(description = "Updated list of root-level tasks for this project", required = true)
    @Valid
    private List<TaskUpdateRequest> tasks;

    // Constructors
    public ProjectUpdateRequest() {
    }

    public ProjectUpdateRequest(String name, String description, ProjectStatus status, 
                               LocalDate dueDate, String address, Integer noOfCleaners,
                               List<TaskUpdateRequest> tasks) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.address = address;
        this.noOfCleaners = noOfCleaners;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNoOfCleaners() {
        return noOfCleaners;
    }

    public void setNoOfCleaners(Integer noOfCleaners) {
        this.noOfCleaners = noOfCleaners;
    }

    public List<TaskUpdateRequest> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskUpdateRequest> tasks) {
        this.tasks = tasks;
    }
}
