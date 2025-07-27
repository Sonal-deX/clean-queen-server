package com.enterprise.cleanqueen.dto.common;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standardized error response format")
public class ApiErrorResponse {
    
    @Schema(description = "Indicates if the operation was successful", example = "false")
    private boolean success;
    
    @Schema(description = "Error message describing what went wrong", example = "Email is already registered")
    private String message;
    
    @Schema(description = "HTTP status code", example = "400")
    private int statusCode;
    
    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;
    
    @Schema(description = "Request path where error occurred", example = "/api/auth/register")
    private String path;
    
    @Schema(description = "Detailed validation errors (if applicable)")
    private Map<String, String> validationErrors;
    
    // Constructors
    public ApiErrorResponse() {
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiErrorResponse(boolean success, String message, int statusCode, LocalDateTime timestamp, String path) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.path = path;
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
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public Map<String, String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
}
