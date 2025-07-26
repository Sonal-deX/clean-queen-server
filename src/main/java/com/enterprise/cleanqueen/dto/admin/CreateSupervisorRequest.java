// CreateSupervisorRequest.java
package com.enterprise.cleanqueen.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new supervisor account")
public class CreateSupervisorRequest {
    
    @Schema(description = "Username for the supervisor account", example = "supervisor_john", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Schema(description = "Email address for the supervisor", example = "supervisor@cleanqueen.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Schema(description = "Phone number for the supervisor", example = "+1234567890", required = false)
    private String phoneNumber;

    // Constructors
    public CreateSupervisorRequest() {}
    
    public CreateSupervisorRequest(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}