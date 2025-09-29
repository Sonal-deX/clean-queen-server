package com.enterprise.cleanqueen.dto.task;

import java.time.LocalDateTime;
import java.util.List;

public class BulkUpdateTaskStatusResponse {
    
    private boolean success;
    private String message;
    private List<TaskUpdateResult> results;
    private int totalUpdated;
    private int totalFailed;
    private LocalDateTime timestamp;
    
    // Constructors
    public BulkUpdateTaskStatusResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public BulkUpdateTaskStatusResponse(boolean success, String message, List<TaskUpdateResult> results) {
        this();
        this.success = success;
        this.message = message;
        this.results = results;
        if (results != null) {
            this.totalUpdated = (int) results.stream().filter(TaskUpdateResult::isSuccess).count();
            this.totalFailed = results.size() - this.totalUpdated;
        }
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
    
    public List<TaskUpdateResult> getResults() {
        return results;
    }
    
    public void setResults(List<TaskUpdateResult> results) {
        this.results = results;
        if (results != null) {
            this.totalUpdated = (int) results.stream().filter(TaskUpdateResult::isSuccess).count();
            this.totalFailed = results.size() - this.totalUpdated;
        }
    }
    
    public int getTotalUpdated() {
        return totalUpdated;
    }
    
    public void setTotalUpdated(int totalUpdated) {
        this.totalUpdated = totalUpdated;
    }
    
    public int getTotalFailed() {
        return totalFailed;
    }
    
    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}