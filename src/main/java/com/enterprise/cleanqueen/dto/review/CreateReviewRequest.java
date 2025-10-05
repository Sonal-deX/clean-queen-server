package com.enterprise.cleanqueen.dto.review;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    @Schema(description = "Optional review images (maximum 2)", maxLength = 2)
    @Size(max = 2, message = "Maximum 2 images are allowed")
    private List<MultipartFile> images;

    // Constructors
    public CreateReviewRequest() {
    }

    public CreateReviewRequest(String taskId, Integer rating, String comment) {
        this.taskId = taskId;
        this.rating = rating;
        this.comment = comment;
    }

    public CreateReviewRequest(String taskId, Integer rating, String comment, List<MultipartFile> images) {
        this.taskId = taskId;
        this.rating = rating;
        this.comment = comment;
        this.images = images;
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

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
