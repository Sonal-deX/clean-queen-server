package com.enterprise.cleanqueen.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "reviews")
public class Review {
    
    @Id
    @Size(min = 6, max = 6, message = "Review ID must be exactly 6 characters")
    @Column(name = "id", length = 6, nullable = false)
    private String id;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    
    @NotBlank(message = "Task ID is required")
    @Column(name = "task_id", length = 8, unique = true, nullable = false)
    private String taskId; // Foreign Key to Task (mandatory, one-to-one)
    
    @NotBlank(message = "Customer ID is required")
    @Column(name = "customer_id", length = 6, nullable = false)
    private String customerId; // Foreign Key to User (mandatory) - Customer who gives the rating
    
    @Column(name = "image_url_1", length = 500)
    private String imageUrl1; // First image URL (optional)
    
    @Column(name = "image_url_2", length = 500)
    private String imageUrl2; // Second image URL (optional)
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Review() {}
    
    public Review(String id, Integer rating, String comment, String taskId, String customerId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.taskId = taskId;
        this.customerId = customerId;
    }
    
    public Review(String id, Integer rating, String comment, String taskId, String customerId, String imageUrl1, String imageUrl2) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.taskId = taskId;
        this.customerId = customerId;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getImageUrl1() { return imageUrl1; }
    public void setImageUrl1(String imageUrl1) { this.imageUrl1 = imageUrl1; }
    
    public String getImageUrl2() { return imageUrl2; }
    public void setImageUrl2(String imageUrl2) { this.imageUrl2 = imageUrl2; }
}