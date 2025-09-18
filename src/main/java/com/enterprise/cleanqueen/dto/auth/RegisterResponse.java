package com.enterprise.cleanqueen.dto.auth;

import java.time.LocalDateTime;

public class RegisterResponse {
    
   
    private boolean success;
    
    
    private String message;
    
    
    private String email;
    
    
    private LocalDateTime timestamp;
    
    // Constructors
    public RegisterResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public RegisterResponse(boolean success, String message, String email) {
        this();
        this.success = success;
        this.message = message;
        this.email = email;
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}