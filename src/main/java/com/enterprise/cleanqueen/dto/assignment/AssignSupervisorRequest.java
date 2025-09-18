package com.enterprise.cleanqueen.dto.assignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Supervisor self-assignment to project")
public class AssignSupervisorRequest {

    @Schema(description = "Project code for assignment", example = "X3Y-5Z7", required = true)
    @NotBlank(message = "Project code is required")
    @Pattern(regexp = "^[A-Z0-9]{3}-[A-Z0-9]{3}$", message = "Project code must be in XXX-XXX format")
    private String projectCode;

    // Constructors
    public AssignSupervisorRequest() {
    }

    public AssignSupervisorRequest(String projectCode) {
        this.projectCode = projectCode;
    }

    // Getters and Setters
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
