// CreateSupervisorRequest.java
package com.enterprise.cleanqueen.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new supervisor account")
public class CreateSupervisorRequest {
    
    @Schema(description = "First name for the supervisor", example = "John", required = true)
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;
    
    @Schema(description = "Last name for the supervisor", example = "Doe", required = false)
    @Size(max = 50, message = "Last name must be maximum 50 characters")
    private String lastName;
    
    @Schema(description = "Email address for the supervisor", example = "supervisor@cleanqueen.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Schema(description = "Phone number for the supervisor", example = "+1234567890", required = false)
    private String phoneNumber;

    // Constructors
    public CreateSupervisorRequest() {}
    
    public CreateSupervisorRequest(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}