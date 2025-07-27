package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;

public class SendProjectCodeResponse {

    private boolean success;
    private String message;
    private String email;
    private String projectCode;
    private String projectName;
    private LocalDateTime timestamp;

    // Constructors
    public SendProjectCodeResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public SendProjectCodeResponse(boolean success, String message, String email, String projectCode, String projectName) {
        this();
        this.success = success;
        this.message = message;
        this.email = email;
        this.projectCode = projectCode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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
