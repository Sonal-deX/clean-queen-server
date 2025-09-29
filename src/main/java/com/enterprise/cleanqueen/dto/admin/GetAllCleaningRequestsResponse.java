package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.enterprise.cleanqueen.dto.request.CleaningRequestSummaryDto;

public class GetAllCleaningRequestsResponse {
    
    private boolean success;
    private String message;
    private List<CleaningRequestSummaryDto> cleaningRequests;
    private int totalCount;
    private LocalDateTime timestamp;
    
    // Constructors
    public GetAllCleaningRequestsResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public GetAllCleaningRequestsResponse(boolean success, String message, List<CleaningRequestSummaryDto> cleaningRequests) {
        this();
        this.success = success;
        this.message = message;
        this.cleaningRequests = cleaningRequests;
        this.totalCount = cleaningRequests != null ? cleaningRequests.size() : 0;
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
    
    public List<CleaningRequestSummaryDto> getCleaningRequests() {
        return cleaningRequests;
    }
    
    public void setCleaningRequests(List<CleaningRequestSummaryDto> cleaningRequests) {
        this.cleaningRequests = cleaningRequests;
        this.totalCount = cleaningRequests != null ? cleaningRequests.size() : 0;
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