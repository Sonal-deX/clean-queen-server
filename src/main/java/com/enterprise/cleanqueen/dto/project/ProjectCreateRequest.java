package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Create project with hierarchical tasks")
public class ProjectCreateRequest {

    @Schema(description = "Project name", example = "Faculty Building Cleaning", required = true)
    @NotBlank(message = "Project name is required")
    private String name;

    @Schema(description = "Project description", example = "Complete cleaning of university faculty building")
    private String description;

    @Schema(description = "Project due date", example = "2024-01-15", required = true)
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @Schema(description = "Project address", example = "123 University Ave, Faculty Building, Room 101", required = true)
    @NotBlank(message = "Address is required")
    private String address;

    @Schema(description = "Number of cleaners required", example = "3", required = true)
    @NotNull(message = "Number of cleaners is required")
    @Min(value = 1, message = "Number of cleaners must be at least 1")
    private Integer noOfCleaners;

    @Schema(description = "List of root-level tasks for this project", required = true)
    @Valid
    private List<TaskCreateRequest> tasks;

    // Constructors
    public ProjectCreateRequest() {
    }

    public ProjectCreateRequest(String name, String description, LocalDate dueDate, 
                               String address, Integer noOfCleaners, List<TaskCreateRequest> tasks) {
        this.name = name;
        this.description = description;
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

    public List<TaskCreateRequest> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskCreateRequest> tasks) {
        this.tasks = tasks;
    }
}
