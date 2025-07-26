package com.enterprise.cleanqueen.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update user profile information")
public class UpdateProfileRequest {
    
    @Schema(description = "New username (optional)", example = "john_doe_updated", required = false)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Schema(description = "New phone number (optional)", example = "+1987654321", required = false)
    private String phoneNumber;
    
    @Schema(description = "New password (optional, requires current password)", example = "NewSecurePassword123", required = false)
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;
    
    @Schema(description = "Current password (required only when changing password)", example = "CurrentPassword123", required = false)
    private String currentPassword; // Required if changing password

    // Constructors
    public UpdateProfileRequest() {}
    
    public UpdateProfileRequest(String username, String phoneNumber, String newPassword, String currentPassword) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
        this.currentPassword = currentPassword;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
}