package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;

public class CreateSupervisorResponse {
    
    private boolean success;
    private String message;
    private String supervisorId;
    private String email;
    private String username;
    private LocalDateTime timestamp;
    
    // Constructors
    public CreateSupervisorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public CreateSupervisorResponse(boolean success, String message, String supervisorId, String email, String username) {
        this();
        this.success = success;
        this.message = message;
        this.supervisorId = supervisorId;
        this.email = email;
        this.username = username;
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
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}