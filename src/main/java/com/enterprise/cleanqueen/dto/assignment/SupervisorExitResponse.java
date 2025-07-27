package com.enterprise.cleanqueen.dto.assignment;

import java.time.LocalDateTime;

public class SupervisorExitResponse {

    private boolean success;
    private String message;
    private String projectId;
    private String projectName;
    private LocalDateTime timestamp;

    // Constructors
    public SupervisorExitResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public SupervisorExitResponse(boolean success, String message, String projectId, String projectName) {
        this();
        this.success = success;
        this.message = message;
        this.projectId = projectId;
        this.projectName = projectName;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
