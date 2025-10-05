package com.enterprise.cleanqueen.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.enterprise.cleanqueen.enums.CleaningRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cleaning_requests")
public class CleaningRequest {
    
    @Id
    @Size(min = 10, max = 10, message = "Request ID must be exactly 10 characters")
    @Column(name = "id", length = 10, nullable = false)
    private String id;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @NotBlank(message = "Service address is required")
    @Column(name = "service_address", nullable = false)
    private String serviceAddress;
    
    @NotBlank(message = "Service type is required")
    @Column(name = "service_type", nullable = false)
    private String serviceType;
    
    @Column(name = "preferred_time")
    private String preferredTime;
    
    @Column(name = "additional_details", columnDefinition = "TEXT")
    private String additionalDetails;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CleaningRequestStatus status = CleaningRequestStatus.PENDING;
    
    @Column(name = "user_id", length = 6)
    private String userId; // Foreign Key to User (nullable)
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public CleaningRequest() {}
    
    public CleaningRequest(String id, String name, String email, String phoneNumber, 
                          String serviceAddress, String serviceType, String preferredTime, 
                          String additionalDetails) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.serviceAddress = serviceAddress;
        this.serviceType = serviceType;
        this.preferredTime = preferredTime;
        this.additionalDetails = additionalDetails;
        this.status = CleaningRequestStatus.PENDING;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getServiceAddress() { return serviceAddress; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }
    
    public String getAdditionalDetails() { return additionalDetails; }
    public void setAdditionalDetails(String additionalDetails) { this.additionalDetails = additionalDetails; }
    
    public CleaningRequestStatus getStatus() { return status; }
    public void setStatus(CleaningRequestStatus status) { this.status = status; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}