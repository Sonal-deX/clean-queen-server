package com.enterprise.cleanqueen.dto.common;

import java.time.LocalDateTime;


public class ApiSuccessResponse<T> {
    
    
    private boolean success;
    
    
    private String message;
    
    
    private T data;
    
    
    private LocalDateTime timestamp;
    
    // Constructors
    public ApiSuccessResponse() {
        this.success = true;
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiSuccessResponse(String message) {
        this();
        this.message = message;
    }
    
    public ApiSuccessResponse(String message, T data) {
        this(message);
        this.data = data;
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}