package com.enterprise.cleanqueen.dto.user;

import java.time.LocalDateTime;

public class UpdateProfileResponse {
    
    private boolean success;
    private String message;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime timestamp;
    
    // Constructors
    public UpdateProfileResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public UpdateProfileResponse(boolean success, String message, String firstName, String lastName, String phoneNumber) {
        this();
        this.success = success;
        this.message = message;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}