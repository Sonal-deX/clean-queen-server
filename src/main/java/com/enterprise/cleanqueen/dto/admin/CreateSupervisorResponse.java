package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;

public class CreateSupervisorResponse {
    
    private boolean success;
    private String message;
    private String supervisorId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime timestamp;
    
    // Constructors
    public CreateSupervisorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public CreateSupervisorResponse(boolean success, String message, String supervisorId, String email, String firstName, String lastName) {
        this();
        this.success = success;
        this.message = message;
        this.supervisorId = supervisorId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}