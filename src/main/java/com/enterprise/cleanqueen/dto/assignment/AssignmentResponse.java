package com.enterprise.cleanqueen.dto.assignment;

import java.time.LocalDateTime;

public class AssignmentResponse {

    private boolean success;
    private String message;
    private String projectId;
    private String projectCode;
    private String projectName;
    private String assignedRole;
    private LocalDateTime timestamp;

    // Constructors
    public AssignmentResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public AssignmentResponse(boolean success, String message, String projectId, String projectCode, String projectName, String assignedRole) {
        this();
        this.success = success;
        this.message = message;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.assignedRole = assignedRole;
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

    public String getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(String assignedRole) {
        this.assignedRole = assignedRole;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
