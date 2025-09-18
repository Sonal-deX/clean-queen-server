package com.enterprise.cleanqueen.dto.request;

import java.time.LocalDateTime;

public class CreateRequestResponse {

    private boolean success;
    private String message;
    private String requestId;
    private String status;
    private LocalDateTime timestamp;

    // Constructors
    public CreateRequestResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public CreateRequestResponse(boolean success, String message, String requestId, String status) {
        this();
        this.success = success;
        this.message = message;
        this.requestId = requestId;
        this.status = status;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
