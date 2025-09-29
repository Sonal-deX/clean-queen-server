package com.enterprise.cleanqueen.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.enterprise.cleanqueen.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @Size(min = 6, max = 6, message = "Project ID must be exactly 6 characters")
    @Column(name = "id", length = 6, nullable = false)
    private String id;
    
    @NotBlank(message = "Project name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Pattern(regexp = "^[A-Z0-9]{3}-[A-Z0-9]{3}$", message = "Project code must be in XXX-XXX format")
    @Column(name = "project_code", length = 7, unique = true, nullable = false)
    private String projectCode;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status = ProjectStatus.PENDING_ASSIGNMENT;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Min(value = 1, message = "Number of cleaners must be at least 1")
    @Column(name = "no_of_cleaners")
    private Integer noOfCleaners;
    
    @Column(name = "average_rating")
    private Float averageRating;
    
    @Column(name = "customer_id", length = 6)
    private String customerId; // Foreign Key to User (nullable)
    
    @Column(name = "supervisor_id", length = 6)
    private String supervisorId; // Foreign Key to User (nullable)
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Project() {}
    
    public Project(String id, String name, String description, String projectCode, 
                   LocalDate dueDate, String address, Integer noOfCleaners) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectCode = projectCode;
        this.dueDate = dueDate;
        this.address = address;
        this.noOfCleaners = noOfCleaners;
        this.status = ProjectStatus.PENDING_ASSIGNMENT;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Integer getNoOfCleaners() { return noOfCleaners; }
    public void setNoOfCleaners(Integer noOfCleaners) { this.noOfCleaners = noOfCleaners; }
    
    public Float getAverageRating() { return averageRating; }
    public void setAverageRating(Float averageRating) { this.averageRating = averageRating; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}