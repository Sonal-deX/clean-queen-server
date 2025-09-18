package com.enterprise.cleanqueen.dto.assignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Supervisor exit from project")
public class SupervisorExitRequest {

    @Schema(description = "Project ID to exit from", example = "ABC123", required = true)
    @NotBlank(message = "Project ID is required")
    private String projectId;

    // Constructors
    public SupervisorExitRequest() {
    }

    public SupervisorExitRequest(String projectId) {
        this.projectId = projectId;
    }

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
