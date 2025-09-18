package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDateTime;

public class ProjectCreateResponse {

    private boolean success;
    private String message;
    private String projectId;
    private String projectCode;
    private String name;
    private int totalTasks;
    private LocalDateTime timestamp;

    // Constructors
    public ProjectCreateResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ProjectCreateResponse(boolean success, String message, String projectId, String projectCode, String name, int totalTasks) {
        this();
        this.success = success;
        this.message = message;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.name = name;
        this.totalTasks = totalTasks;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
