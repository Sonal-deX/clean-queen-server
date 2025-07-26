package com.enterprise.cleanqueen.dto.common;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final boolean success = false; // Always false for errors
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}