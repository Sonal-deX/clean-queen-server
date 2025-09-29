package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.enterprise.cleanqueen.dto.user.UserSummaryDto;

public class GetAllCustomersResponse {
    
    private boolean success;
    private String message;
    private List<UserSummaryDto> customers;
    private int totalCount;
    private LocalDateTime timestamp;
    
    // Constructors
    public GetAllCustomersResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public GetAllCustomersResponse(boolean success, String message, List<UserSummaryDto> customers) {
        this();
        this.success = success;
        this.message = message;
        this.customers = customers;
        this.totalCount = customers != null ? customers.size() : 0;
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
    
    public List<UserSummaryDto> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<UserSummaryDto> customers) {
        this.customers = customers;
        this.totalCount = customers != null ? customers.size() : 0;
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