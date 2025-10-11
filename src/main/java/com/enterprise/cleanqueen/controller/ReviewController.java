package com.enterprise.cleanqueen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.dto.review.CreateReviewRequest;
import com.enterprise.cleanqueen.dto.review.CreateReviewResponse;
import com.enterprise.cleanqueen.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reviews")
@Tag(name = "⭐ Task Reviews", description = "Task review and rating system with automatic propagation")
@SecurityRequirement(name = "JWT Authentication")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Operation(
            summary = "Create Task Review",
            description = """
        **Create a review for a completed leaf task with intelligent rating propagation and optional image uploads.**
        
        **Customer Only Access:**
        - Only customers who own the project can create reviews
        - Reviews can only be created for "leaf tasks" (tasks with no subtasks)
        - One review per task (one-to-one relationship)
        
        **Image Upload Features:**
        - Optional: Upload up to 2 images with your review
        - Supported formats: JPEG, PNG, GIF, WebP (max 25MB each)
        - **Automatic Image Compression:** Images are automatically compressed to 500-1000KB for optimal performance
        - Images stored securely on Cloudflare CDN
        - If image upload fails, entire review creation is rolled back
        
        **Intelligent Rating Propagation:**
        1. Review created on leaf task → Task gets the review rating
        2. System checks parent task → If ALL sibling tasks have ratings → Calculate parent's average
        3. Propagation bubbles up through ALL parent levels automatically
        4. Finally → Project rating = average of all root-level task ratings
        
        **Example Propagation:**
        ```
        Project: Faculty Building (Rating: 4.0)
        ├── First Floor (Rating: 4.0)
        │   ├── Lab 1 (Rating: 4.5)
        │   │   ├── Organize Wires (Rating: 5) ✅ REVIEWED
        │   │   └── Clean Floor (Rating: 4) ✅ REVIEWED  
        │   └── Washroom (Rating: 3.5) ✅ REVIEWED
        └── Second Floor (Rating: 4.0) ✅ REVIEWED
        ```
        
        **Request Format:** Multipart Form Data
        - taskId: string (required)
        - rating: integer 1-5 (required)
        - comment: string (optional)
        - images: file array max 2 items (optional)
        
        **Business Rules:**
        - Only leaf tasks (no children) can be reviewed
        - Customer must own the project
        - Task must belong to customer's project
        - Automatic rating calculation and propagation
        - Images uploaded to CDN before review creation
        """,
            tags = {"Task Reviews"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Review created successfully with rating propagation and image URLs",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CreateReviewResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Review creation failed - Invalid task, not a leaf task, already reviewed, or image upload error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "❌ Unauthorized - Invalid or missing authentication token",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "❌ Access denied - Customer does not own this project",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createReview(
            @Parameter(description = "Task ID to review", required = true)
            @RequestParam("taskId") @NotBlank(message = "Task ID is required") String taskId,
            
            @Parameter(description = "Rating from 1 to 5", required = true)
            @RequestParam("rating") @NotNull(message = "Rating is required") 
            @Min(value = 1, message = "Rating must be at least 1") 
            @Max(value = 5, message = "Rating must be at most 5") Integer rating,
            
            @Parameter(description = "Review comment", required = false)
            @RequestParam(value = "comment", required = false) String comment,
            
            @Parameter(description = "Optional review images (maximum 2)", required = false)
            @RequestParam(value = "images", required = false) 
            @Size(max = 2, message = "Maximum 2 images are allowed") List<MultipartFile> images,
            
            Authentication authentication) {

        // Create request object
        CreateReviewRequest request = new CreateReviewRequest();
        request.setTaskId(taskId);
        request.setRating(rating);
        request.setComment(comment);
        request.setImages(images);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CreateReviewResponse response = reviewService.createReview(request, userDetails.getUsername());
        logger.info("Review created for task: {} with rating: {}", response.getTaskId(), response.getRating());
        return ResponseEntity.ok(response);
    }
}
