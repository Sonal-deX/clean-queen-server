package com.enterprise.cleanqueen.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Send project code to customer")
public class SendProjectCodeRequest {

    @Schema(description = "Customer email address", example = "customer@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Project code to send", example = "X3Y-5Z7", required = true)
    @NotBlank(message = "Project code is required")
    @Pattern(regexp = "^[A-Z0-9]{3}-[A-Z0-9]{3}$", message = "Project code must be in XXX-XXX format")
    private String projectCode;

    // Constructors
    public SendProjectCodeRequest() {
    }

    public SendProjectCodeRequest(String email, String projectCode) {
        this.email = email;
        this.projectCode = projectCode;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
