package com.enterprise.cleanqueen.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.enterprise.cleanqueen.enums.CleaningRequestStatus;

public class CleaningRequestSummaryDto {
    
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String serviceAddress;
    private String serviceType;
    private LocalDate preferredDate;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private String additionalDetails;
    private CleaningRequestStatus status;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public CleaningRequestSummaryDto() {}
    
    public CleaningRequestSummaryDto(String id, String name, String email, String phoneNumber, 
                                   String serviceAddress, String serviceType, LocalDate preferredDate,
                                   LocalTime timeFrom, LocalTime timeTo, String additionalDetails, 
                                   CleaningRequestStatus status, String userId,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.serviceAddress = serviceAddress;
        this.serviceType = serviceType;
        this.preferredDate = preferredDate;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.additionalDetails = additionalDetails;
        this.status = status;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getServiceAddress() {
        return serviceAddress;
    }
    
    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
    
    public String getServiceType() {
        return serviceType;
    }
    
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    public LocalDate getPreferredDate() {
        return preferredDate;
    }
    
    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }
    
    public LocalTime getTimeFrom() {
        return timeFrom;
    }
    
    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }
    
    public LocalTime getTimeTo() {
        return timeTo;
    }
    
    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }
    
    public String getAdditionalDetails() {
        return additionalDetails;
    }
    
    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
    
    public CleaningRequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(CleaningRequestStatus status) {
        this.status = status;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}