package com.enterprise.cleanqueen.dto.project;

import java.time.LocalDateTime;
import java.util.List;

import com.enterprise.cleanqueen.enums.ProjectStatus;
import com.enterprise.cleanqueen.enums.TaskPriority;
import com.enterprise.cleanqueen.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for project with complete task hierarchy")
public class ProjectTaskHierarchyResponse {
    
    @Schema(description = "Success status", example = "true")
    private boolean success;
    
    @Schema(description = "Response message", example = "Project details retrieved successfully")
    private String message;
    
    @Schema(description = "Project details with task hierarchy")
    private ProjectDetails project;
    
    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;
    
    // Constructors
    public ProjectTaskHierarchyResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ProjectTaskHierarchyResponse(boolean success, String message, ProjectDetails project) {
        this.success = success;
        this.message = message;
        this.project = project;
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
    
    public ProjectDetails getProject() {
        return project;
    }
    
    public void setProject(ProjectDetails project) {
        this.project = project;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Schema(description = "Detailed project information with task hierarchy")
    public static class ProjectDetails {
        
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
        
        @Schema(description = "Root tasks (top-level tasks without parent)")
        private List<TaskHierarchy> rootTasks;
        
        @Schema(description = "Total number of tasks in project", example = "15")
        private int totalTasks;
        
        @Schema(description = "Number of completed tasks", example = "8")
        private int completedTasks;
        
        @Schema(description = "Project creation date")
        private LocalDateTime createdAt;
        
        @Schema(description = "Project last update date")
        private LocalDateTime updatedAt;
        
        // Constructors
        public ProjectDetails() {}
        
        public ProjectDetails(String projectId, String projectCode, String projectName, 
                            String description, ProjectStatus status, List<TaskHierarchy> rootTasks,
                            int totalTasks, int completedTasks, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.projectId = projectId;
            this.projectCode = projectCode;
            this.projectName = projectName;
            this.description = description;
            this.status = status;
            this.rootTasks = rootTasks;
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
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
        
        public List<TaskHierarchy> getRootTasks() {
            return rootTasks;
        }
        
        public void setRootTasks(List<TaskHierarchy> rootTasks) {
            this.rootTasks = rootTasks;
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
    
    @Schema(description = "Task with nested subtask hierarchy")
    public static class TaskHierarchy {
        
        @Schema(description = "Task ID", example = "TSK12345")
        private String taskId;
        
        @Schema(description = "Task name", example = "Clean First Floor")
        private String taskName;
        
        @Schema(description = "Task description", example = "Clean all areas on the first floor")
        private String description;
        
        @Schema(description = "Task status")
        private TaskStatus status;
        
        @Schema(description = "Task priority")
        private TaskPriority priority;
        
        @Schema(description = "Estimated duration in hours", example = "4.5")
        private Double estimatedHours;
        
        @Schema(description = "Assigned user ID", example = "USER123")
        private String assignedUserId;
        
        @Schema(description = "Assigned user name", example = "John Doe")
        private String assignedUserName;
        
        @Schema(description = "Due date")
        private LocalDateTime dueDate;
        
        @Schema(description = "Task average rating", example = "4.5")
        private Float averageRating;
        
        @Schema(description = "Task creation date")
        private LocalDateTime createdAt;
        
        @Schema(description = "Task last update date")
        private LocalDateTime updatedAt;
        
        @Schema(description = "Subtasks under this task")
        private List<TaskHierarchy> subtasks;
        
        // Constructors
        public TaskHierarchy() {}
        
        public TaskHierarchy(String taskId, String taskName, String description, 
                           TaskStatus status, TaskPriority priority, Double estimatedHours,
                           String assignedUserId, String assignedUserName, LocalDateTime dueDate,
                           Float averageRating, LocalDateTime createdAt, LocalDateTime updatedAt, 
                           List<TaskHierarchy> subtasks) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.description = description;
            this.status = status;
            this.priority = priority;
            this.estimatedHours = estimatedHours;
            this.assignedUserId = assignedUserId;
            this.assignedUserName = assignedUserName;
            this.dueDate = dueDate;
            this.averageRating = averageRating;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.subtasks = subtasks;
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
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public TaskStatus getStatus() {
            return status;
        }
        
        public void setStatus(TaskStatus status) {
            this.status = status;
        }
        
        public TaskPriority getPriority() {
            return priority;
        }
        
        public void setPriority(TaskPriority priority) {
            this.priority = priority;
        }
        
        public Double getEstimatedHours() {
            return estimatedHours;
        }
        
        public void setEstimatedHours(Double estimatedHours) {
            this.estimatedHours = estimatedHours;
        }
        
        public String getAssignedUserId() {
            return assignedUserId;
        }
        
        public void setAssignedUserId(String assignedUserId) {
            this.assignedUserId = assignedUserId;
        }
        
        public String getAssignedUserName() {
            return assignedUserName;
        }
        
        public void setAssignedUserName(String assignedUserName) {
            this.assignedUserName = assignedUserName;
        }
        
        public LocalDateTime getDueDate() {
            return dueDate;
        }
        
        public void setDueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
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
        
        public List<TaskHierarchy> getSubtasks() {
            return subtasks;
        }
        
        public void setSubtasks(List<TaskHierarchy> subtasks) {
            this.subtasks = subtasks;
        }
    }
}