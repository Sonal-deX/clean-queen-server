package com.enterprise.cleanqueen.dto.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.enterprise.cleanqueen.enums.ProjectStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for getting all projects (Admin only)")
public class GetAllProjectsResponse {
    
    @Schema(description = "Success status", example = "true")
    private boolean success;
    
    @Schema(description = "Response message", example = "All projects retrieved successfully")
    private String message;
    
    @Schema(description = "List of all projects in the system")
    private List<ProjectInfo> projects;
    
    @Schema(description = "Total number of projects", example = "15")
    private int totalProjects;
    
    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;
    
    // Constructors
    public GetAllProjectsResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public GetAllProjectsResponse(boolean success, String message, List<ProjectInfo> projects) {
        this.success = success;
        this.message = message;
        this.projects = projects;
        this.totalProjects = projects != null ? projects.size() : 0;
        this.timestamp = LocalDateTime.now();
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
    
    public List<ProjectInfo> getProjects() {
        return projects;
    }
    
    public void setProjects(List<ProjectInfo> projects) {
        this.projects = projects;
        this.totalProjects = projects != null ? projects.size() : 0;
    }
    
    public int getTotalProjects() {
        return totalProjects;
    }
    
    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Schema(description = "Project information for admin view")
    public static class ProjectInfo {
        
        @Schema(description = "Project ID", example = "PROJ12")
        private String projectId;
        
        @Schema(description = "Project code", example = "CLEAN001")
        private String projectCode;
        
        @Schema(description = "Project name", example = "Faculty Building Cleaning")
        private String projectName;
        
        @Schema(description = "Project description", example = "Complete cleaning of faculty building")
        private String description;
        
        @Schema(description = "Project status")
        private ProjectStatus status;
        
        @Schema(description = "Project due date")
        private LocalDate dueDate;
        
        @Schema(description = "Project address", example = "123 Main Street, City")
        private String address;
        
        @Schema(description = "Number of cleaners required", example = "3")
        private Integer noOfCleaners;
        
        @Schema(description = "Customer ID", example = "USER01")
        private String customerId;
        
        @Schema(description = "Customer name", example = "John Doe")
        private String customerName;
        
        @Schema(description = "Supervisor ID", example = "SUP01")
        private String supervisorId;
        
        @Schema(description = "Supervisor name", example = "Jane Smith")
        private String supervisorName;
        
        @Schema(description = "Number of total tasks", example = "5")
        private int totalTasks;
        
        @Schema(description = "Number of completed tasks", example = "2")
        private int completedTasks;
        
        @Schema(description = "Project average rating", example = "4.5")
        private Float averageRating;
        
        @Schema(description = "Project creation date")
        private LocalDateTime createdAt;
        
        @Schema(description = "Project last update date")
        private LocalDateTime updatedAt;
        
        // Constructors
        public ProjectInfo() {}
        
        public ProjectInfo(String projectId, String projectCode, String projectName, 
                          String description, ProjectStatus status, LocalDate dueDate, 
                          String address, Integer noOfCleaners, String customerId, 
                          String customerName, String supervisorId, String supervisorName,
                          int totalTasks, int completedTasks, Float averageRating, 
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.projectId = projectId;
            this.projectCode = projectCode;
            this.projectName = projectName;
            this.description = description;
            this.status = status;
            this.dueDate = dueDate;
            this.address = address;
            this.noOfCleaners = noOfCleaners;
            this.customerId = customerId;
            this.customerName = customerName;
            this.supervisorId = supervisorId;
            this.supervisorName = supervisorName;
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
            this.averageRating = averageRating;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
        
        // Getters and Setters
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
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public ProjectStatus getStatus() {
            return status;
        }
        
        public void setStatus(ProjectStatus status) {
            this.status = status;
        }
        
        public LocalDate getDueDate() {
            return dueDate;
        }
        
        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public Integer getNoOfCleaners() {
            return noOfCleaners;
        }
        
        public void setNoOfCleaners(Integer noOfCleaners) {
            this.noOfCleaners = noOfCleaners;
        }
        
        public String getCustomerId() {
            return customerId;
        }
        
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
        
        public String getCustomerName() {
            return customerName;
        }
        
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
        
        public String getSupervisorId() {
            return supervisorId;
        }
        
        public void setSupervisorId(String supervisorId) {
            this.supervisorId = supervisorId;
        }
        
        public String getSupervisorName() {
            return supervisorName;
        }
        
        public void setSupervisorName(String supervisorName) {
            this.supervisorName = supervisorName;
        }
        
        public int getTotalTasks() {
            return totalTasks;
        }
        
        public void setTotalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
        }
        
        public int getCompletedTasks() {
            return completedTasks;
        }
        
        public void setCompletedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
        }
        
        public Float getAverageRating() {
            return averageRating;
        }
        
        public void setAverageRating(Float averageRating) {
            this.averageRating = averageRating;
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
}