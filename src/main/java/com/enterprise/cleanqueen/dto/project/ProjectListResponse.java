package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDateTime;
import java.util.List;

import com.enterprise.cleanqueen.enums.ProjectStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for project list by user")
public class ProjectListResponse {
    
    @Schema(description = "Success status", example = "true")
    private boolean success;
    
    @Schema(description = "Response message", example = "Projects retrieved successfully")
    private String message;
    
    @Schema(description = "List of projects assigned to the user")
    private List<ProjectSummary> projects;
    
    @Schema(description = "Total number of projects", example = "3")
    private int totalProjects;
    
    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;
    
    // Constructors
    public ProjectListResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ProjectListResponse(boolean success, String message, List<ProjectSummary> projects) {
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
    
    public List<ProjectSummary> getProjects() {
        return projects;
    }
    
    public void setProjects(List<ProjectSummary> projects) {
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
    
    @Schema(description = "Project summary information")
    public static class ProjectSummary {
        
        @Schema(description = "Project ID", example = "12345")
        private String projectId;
        
        @Schema(description = "Project code", example = "ABC123")
        private String projectCode;
        
        @Schema(description = "Project name", example = "Faculty Building Cleaning")
        private String projectName;
        
        @Schema(description = "Project description", example = "Complete cleaning of faculty building")
        private String description;
        
        @Schema(description = "Project status")
        private ProjectStatus status;
        
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
        public ProjectSummary() {}
        
        public ProjectSummary(String projectId, String projectCode, String projectName, 
                            String description, ProjectStatus status, int totalTasks, 
                            int completedTasks, Float averageRating, LocalDateTime createdAt, 
                            LocalDateTime updatedAt) {
            this.projectId = projectId;
            this.projectCode = projectCode;
            this.projectName = projectName;
            this.description = description;
            this.status = status;
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