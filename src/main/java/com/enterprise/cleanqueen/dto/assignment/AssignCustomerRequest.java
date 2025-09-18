package com.enterprise.cleanqueen.dto.assignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Customer self-assignment to project")
public class AssignCustomerRequest {

    @Schema(description = "Project code for assignment", example = "X3Y-5Z7", required = true)
    @NotBlank(message = "Project code is required")
    @Pattern(regexp = "^[A-Z0-9]{3}-[A-Z0-9]{3}$", message = "Project code must be in XXX-XXX format")
    private String projectCode;

    // Constructors
    public AssignCustomerRequest() {
    }

    public AssignCustomerRequest(String projectCode) {
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
