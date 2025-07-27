package com.enterprise.cleanqueen.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Create task review")
public class CreateReviewRequest {

    @Schema(description = "Task ID to review", example = "ABCD1234", required = true)
    @NotBlank(message = "Task ID is required")
    private String taskId;

    @Schema(description = "Rating from 1 to 5", example = "4", required = true)
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Schema(description = "Review comment", example = "Excellent work, very thorough cleaning")
    private String comment;

    // Constructors
    public CreateReviewRequest() {
    }

    public CreateReviewRequest(String taskId, Integer rating, String comment) {
        this.taskId = taskId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
