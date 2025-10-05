package com.enterprise.cleanqueen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.dto.task.UpdateTaskStatusRequest;
import com.enterprise.cleanqueen.dto.task.BulkUpdateTaskStatusResponse;
import com.enterprise.cleanqueen.service.TaskService;

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
@RequestMapping("/tasks")
@Tag(name = "📋 Task Management", description = "Supervisor task management endpoints")
@SecurityRequirement(name = "JWT Authentication")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Operation(
            summary = "Bulk Update Task Status",
            description = """
        **Update the status of multiple tasks at once (Supervisor Only).**
        
        **Supervisor Only Access:**
        - Only users with SUPERVISOR role can update task status
        - Can update multiple tasks in a single request
        - Each task update is processed individually, partial success possible
        - Common status updates: PENDING → IN_PROGRESS → COMPLETED
        
        **Request Body Format:**
        ```json
        {
          "tasks": [
            {
              "taskId": "TASK001",
              "status": "COMPLETED",
              "remarks": "Task completed successfully"
            },
            {
              "taskId": "TASK002", 
              "status": "IN_PROGRESS",
              "remarks": "Started working on this task"
            }
          ]
        }
        ```
        
        **Available Task Statuses:**
        - PENDING_ASSIGNMENT: Task is waiting to be assigned
        - IN_PROGRESS: Task is currently being worked on
        - PENDING_REVIEW: Task is completed and awaiting review
        - COMPLETED: Task has been finished and approved
        - ON_HOLD: Task is temporarily paused
        - CANCELLED: Task has been cancelled
        
        **Process:**
        1. Supervisor provides task ID and new status
        2. System validates task exists and supervisor has permission
        3. Task status is updated with timestamp
        4. Response includes previous and new status for tracking
        """,
            tags = {"Task Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Task status updates processed (check individual results)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = BulkUpdateTaskStatusResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Update failed - Invalid task ID or status",
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
                description = "❌ Access denied - Supervisor role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Task not found - Invalid task ID",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PutMapping("/status")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> updateTasksStatus(
            @Parameter(description = "Bulk task status update details", required = true)
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BulkUpdateTaskStatusResponse response = taskService.updateTasksStatus(request, userDetails.getUsername());
        
                logger.info("Bulk task status update requested for {} tasks by supervisor: {}", 
                   request.getTasks().size(), userDetails.getUsername());
        
        return ResponseEntity.ok(response);
    }
}