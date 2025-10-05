package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for project tasks with reviews")
public class ProjectTaskReviewsResponse {
    
    @Schema(description = "Success status", example = "true")
    private boolean success;
    
    @Schema(description = "Response message", example = "Project task reviews retrieved successfully")
    private String message;
    
    @Schema(description = "Project ID", example = "PROJ12")
    private String projectId;
    
    @Schema(description = "Project name", example = "Faculty Building Cleaning")
    private String projectName;
    
    @Schema(description = "List of tasks with review information")
    private List<TaskReviewInfo> tasks;
    
    @Schema(description = "Total number of tasks", example = "5")
    private int totalTasks;
    
    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;
    
    // Constructors
    public ProjectTaskReviewsResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ProjectTaskReviewsResponse(boolean success, String message, String projectId, 
                                    String projectName, List<TaskReviewInfo> tasks) {
        this.success = success;
        this.message = message;
        this.projectId = projectId;
        this.projectName = projectName;
        this.tasks = tasks;
        this.totalTasks = tasks != null ? tasks.size() : 0;
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
    
    public String getProjectId() {
        return projectId;
    }
    
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public List<TaskReviewInfo> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<TaskReviewInfo> tasks) {
        this.tasks = tasks;
        this.totalTasks = tasks != null ? tasks.size() : 0;
    }
    
    public int getTotalTasks() {
        return totalTasks;
    }
    
    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Schema(description = "Task information with review details")
    public static class TaskReviewInfo {
        
        @Schema(description = "Task ID", example = "TASK123")
        private String taskId;
        
        @Schema(description = "Task name", example = "Clean Lab 1")
        private String taskName;
        
        @Schema(description = "Review comment", example = "Excellent work, very thorough cleaning")
        private String reviewComment;
        
        @Schema(description = "Task rating", example = "4.5")
        private Float rating;
        
        @Schema(description = "Whether task has been reviewed", example = "true")
        private boolean hasReview;
        
        // Constructors
        public TaskReviewInfo() {}
        
        public TaskReviewInfo(String taskId, String taskName, String reviewComment, 
                            Float rating, boolean hasReview) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.reviewComment = reviewComment;
            this.rating = rating;
            this.hasReview = hasReview;
        }
        
        // Getters and Setters
        public String getTaskId() {
            return taskId;
        }
        
        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
        
        public String getTaskName() {
            return taskName;
        }
        
        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }
        
        public String getReviewComment() {
            return reviewComment;
        }
        
        public void setReviewComment(String reviewComment) {
            this.reviewComment = reviewComment;
        }
        
        public Float getRating() {
            return rating;
        }
        
        public void setRating(Float rating) {
            this.rating = rating;
        }
        
        public boolean isHasReview() {
            return hasReview;
        }
        
        public void setHasReview(boolean hasReview) {
            this.hasReview = hasReview;
        }
    }
}