package com.enterprise.cleanqueen.dto.review;

import java.time.LocalDateTime;
import java.util.List;

public class CreateReviewResponse {

    private boolean success;
    private String message;
    private String reviewId;
    private String taskId;
    private String taskName;
    private Integer rating;
    private boolean ratingPropagated;
    private List<String> imageUrls;
    private LocalDateTime timestamp;

    // Constructors
    public CreateReviewResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public CreateReviewResponse(boolean success, String message, String reviewId, String taskId, String taskName, Integer rating, boolean ratingPropagated) {
        this();
        this.success = success;
        this.message = message;
        this.reviewId = reviewId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.rating = rating;
        this.ratingPropagated = ratingPropagated;
    }

    public CreateReviewResponse(boolean success, String message, String reviewId, String taskId, String taskName, Integer rating, boolean ratingPropagated, List<String> imageUrls) {
        this();
        this.success = success;
        this.message = message;
        this.reviewId = reviewId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.rating = rating;
        this.ratingPropagated = ratingPropagated;
        this.imageUrls = imageUrls;
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

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public boolean isRatingPropagated() {
        return ratingPropagated;
    }

    public void setRatingPropagated(boolean ratingPropagated) {
        this.ratingPropagated = ratingPropagated;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
