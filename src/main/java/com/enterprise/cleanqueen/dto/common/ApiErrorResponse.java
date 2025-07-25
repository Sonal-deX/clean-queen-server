package com.enterprise.cleanqueen.dto.common;

import java.time.LocalDateTime;
import java.util.Map;


public class ApiErrorResponse {
    
    
    private boolean success;
    
    
    private String message;
    
    
    private int statusCode;
    
    
    private LocalDateTime timestamp;
    
    
    private Map<String, String> errors;
    
    // Constructors
    public ApiErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.success = false;
    }
    
    public ApiErrorResponse(String message, int statusCode) {
        this();
        this.message = message;
        this.statusCode = statusCode;
    }
    
    public ApiErrorResponse(String message, int statusCode, Map<String, String> errors) {
        this(message, statusCode);
        this.errors = errors;
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
}