package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.enterprise.cleanqueen.dto.user.UserSummaryDto;

public class GetAllSupervisorsResponse {
    
    private boolean success;
    private String message;
    private List<UserSummaryDto> supervisors;
    private int totalCount;
    private LocalDateTime timestamp;
    
    // Constructors
    public GetAllSupervisorsResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public GetAllSupervisorsResponse(boolean success, String message, List<UserSummaryDto> supervisors) {
        this();
        this.success = success;
        this.message = message;
        this.supervisors = supervisors;
        this.totalCount = supervisors != null ? supervisors.size() : 0;
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
    
    public List<UserSummaryDto> getSupervisors() {
        return supervisors;
    }
    
    public void setSupervisors(List<UserSummaryDto> supervisors) {
        this.supervisors = supervisors;
        this.totalCount = supervisors != null ? supervisors.size() : 0;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}