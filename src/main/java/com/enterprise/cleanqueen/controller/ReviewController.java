package com.enterprise.cleanqueen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import jakarta.validation.Valid;

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
        **Create a review for a completed leaf task with intelligent rating propagation.**
        
        **Supervisor Only Access:**
        - Only supervisors assigned to the project can create reviews
        - Reviews can only be created for "leaf tasks" (tasks with no subtasks)
        - One review per task (one-to-one relationship)
        
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
        
        **Business Rules:**
        - Only leaf tasks (no children) can be reviewed
        - Supervisor must be assigned to the project
        - Task must belong to supervisor's assigned project
        - Automatic rating calculation and propagation
        """,
            tags = {"Task Reviews"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Review created successfully with rating propagation",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CreateReviewResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Review creation failed - Invalid task, not a leaf task, or already reviewed",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "❌ Access denied - Supervisor not assigned to this project",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> createReview(
            @Parameter(description = "Task review details", required = true)
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CreateReviewResponse response = reviewService.createReview(request, userDetails.getUsername());
        logger.info("Review created for task: {} with rating: {}", response.getTaskId(), response.getRating());
        return ResponseEntity.ok(response);
    }
}
