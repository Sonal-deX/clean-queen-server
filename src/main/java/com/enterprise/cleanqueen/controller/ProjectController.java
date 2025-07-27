package com.enterprise.cleanqueen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.common.ErrorResponse;
import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateResponse;
import com.enterprise.cleanqueen.service.ProjectService;

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
@RequestMapping("/projects")
@Tag(name = "🏗️ Project Management", description = "Project and task hierarchy management")
@SecurityRequirement(name = "JWT Authentication")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Operation(
            summary = "Create Project with Tasks",
            description = """
        **Create a new project with hierarchical task structure.**
        
        **Admin Only Access:**
        - Only admins can create projects
        - Supports unlimited nesting of tasks and subtasks
        - Generates unique project code for assignment
        
        **Task Hierarchy Support:**
        - Root tasks have no parent
        - Subtasks can have their own subtasks
        - No limit on nesting depth
        - Each task gets unique 8-character ID
        
        **Example Structure:**
        ```
        Project: Faculty Building
        ├── First Floor (root task)
        │   ├── Lab 1 (subtask)
        │   │   ├── Organize Wires (leaf task)
        │   │   └── Clean Floor (leaf task)
        │   └── Washroom (leaf task)
        └── Second Floor (root task)
        ```
        """,
            tags = {"Project Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Project created successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProjectCreateResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "❌ Access denied - Admin role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProject(
            @Parameter(description = "Project creation details with task hierarchy", required = true)
            @Valid @RequestBody ProjectCreateRequest request) {

        try {
            ProjectCreateResponse response = projectService.createProject(request);
            logger.info("Project created: {} with code: {}", response.getProjectId(), response.getProjectCode());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating project: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(
            summary = "Update Project with Tasks",
            description = """
    **Update existing project with complete task hierarchy management.**
    
    **Admin Only Access:**
    - Update project details (name, description, status)
    - Add new tasks and subtasks to any level
    - Update existing tasks (provide task ID)
    - Remove tasks not included in update request
    - Maintains all relationships and hierarchy
    
    **Task Management:**
    - New tasks: Don't provide ID (system generates)
    - Update tasks: Provide existing task ID
    - Remove tasks: Exclude from request (auto-removed)
    - Hierarchy: Maintained through subtask structure
    
    **Use Cases:**
    - Add additional cleaning areas
    - Update task priorities and due dates
    - Restructure project organization
    - Update project status and details
    """,
            tags = {"Project Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Project updated successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProjectUpdateResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Project not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
    })
    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProject(
            @Parameter(description = "Project ID to update", required = true, example = "ABC123")
            @PathVariable String projectId,
            @Parameter(description = "Project update details with task hierarchy", required = true)
            @Valid @RequestBody ProjectUpdateRequest request) {

        try {
            ProjectUpdateResponse response = projectService.updateProject(projectId, request);
            logger.info("Project updated: {} with {} total tasks", projectId, response.getTotalTasks());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating project {}: {}", projectId, e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
