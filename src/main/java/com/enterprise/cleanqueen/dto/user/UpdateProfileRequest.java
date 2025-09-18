package com.enterprise.cleanqueen.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update user profile information")
public class UpdateProfileRequest {
    
    @Schema(description = "New first name (optional)", example = "John", required = false)
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;
    
    @Schema(description = "New last name (optional)", example = "Doe", required = false)
    @Size(max = 50, message = "Last name must be maximum 50 characters")
    private String lastName;
    
    @Schema(description = "New phone number (optional)", example = "+1987654321", required = false)
    private String phoneNumber;
    
    @Schema(description = "New password (optional, requires current password)", example = "NewSecurePassword123", required = false)
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;
    
    @Schema(description = "Current password (required only when changing password)", example = "CurrentPassword123", required = false)
    private String currentPassword; // Required if changing password

    // Constructors
    public UpdateProfileRequest() {}
    
    public UpdateProfileRequest(String firstName, String lastName, String phoneNumber, String newPassword, String currentPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
        this.currentPassword = currentPassword;
    }
    
    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
}