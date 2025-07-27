package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDateTime;

public class ProjectUpdateResponse {

    private boolean success;
    private String message;
    private String projectId;
    private String projectCode;
    private String name;
    private int totalTasks;
    private int tasksAdded;
    private int tasksUpdated;
    private int tasksRemoved;
    private LocalDateTime timestamp;

    // Constructors
    public ProjectUpdateResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ProjectUpdateResponse(boolean success, String message, String projectId, String projectCode, String name,
            int totalTasks, int tasksAdded, int tasksUpdated, int tasksRemoved) {
        this();
        this.success = success;
        this.message = message;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.name = name;
        this.totalTasks = totalTasks;
        this.tasksAdded = tasksAdded;
        this.tasksUpdated = tasksUpdated;
        this.tasksRemoved = tasksRemoved;
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

    public int getTasksAdded() {
        return tasksAdded;
    }

    public void setTasksAdded(int tasksAdded) {
        this.tasksAdded = tasksAdded;
    }

    public int getTasksUpdated() {
        return tasksUpdated;
    }

    public void setTasksUpdated(int tasksUpdated) {
        this.tasksUpdated = tasksUpdated;
    }

    public int getTasksRemoved() {
        return tasksRemoved;
    }

    public void setTasksRemoved(int tasksRemoved) {
        this.tasksRemoved = tasksRemoved;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
