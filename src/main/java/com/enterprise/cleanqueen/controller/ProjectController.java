package com.enterprise.cleanqueen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;
import com.enterprise.cleanqueen.dto.project.ProjectListResponse;
import com.enterprise.cleanqueen.dto.project.ProjectTaskHierarchyResponse;
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
                responseCode = "401",
                description = "❌ Unauthorized - Invalid or missing authentication token",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "❌ Access denied - Admin role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProject(
            @Parameter(description = "Project creation details with task hierarchy", required = true)
            @Valid @RequestBody ProjectCreateRequest request) {

        ProjectCreateResponse response = projectService.createProject(request);
        logger.info("Project created: {} with code: {}", response.getProjectId(), response.getProjectCode());
        return ResponseEntity.ok(response);
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
                responseCode = "401",
                description = "❌ Unauthorized - Invalid or missing authentication token",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "❌ Access denied - Admin role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Project not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
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

        ProjectUpdateResponse response = projectService.updateProject(projectId, request);
        logger.info("Project updated: {} with {} total tasks", projectId, response.getTotalTasks());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Projects by User ID",
            description = """
        **Retrieve all projects assigned to a specific user.**
        
        **Public Access:**
        - No authentication required
        - Returns projects for any valid user ID
        
        **Returns:**
        - List of projects assigned to the user
        - Project summary with task completion status
        - Total number of assigned projects
        
        **Use Cases:**
        - Customer viewing their assigned projects
        - Supervisor checking their managed projects
        - Public monitoring of project assignments
        """,
            tags = {"Project Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Projects retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProjectListResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ User not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @GetMapping("/user/{userId}")
    @SecurityRequirement(name = "")
    public ResponseEntity<?> getProjectsByUserId(
            @Parameter(description = "User ID to get projects for", required = true, example = "USER123")
            @PathVariable String userId) {

        ProjectListResponse response = projectService.getProjectsByUserId(userId);
        logger.info("Retrieved {} projects for user: {}", response.getTotalProjects(), userId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Project Task Hierarchy",
            description = """
        **Retrieve complete task hierarchy for a specific project.**
        
        **Public Access:**
        - No authentication required
        - Returns task hierarchy for any valid project ID
        
        **Returns:**
        - Project details and metadata
        - Complete task hierarchy with unlimited nesting
        - Task assignments and status information
        - Task completion statistics
        
        **Task Hierarchy Features:**
        - Shows all root tasks (no parent)
        - Recursively includes all subtasks
        - Maintains proper parent-child relationships
        - Includes task information for each level
        
        **Use Cases:**
        - Public viewing of project structure and progress
        - Understanding task dependencies
        - Tracking completion status at all levels
        - Project management and oversight
        """,
            tags = {"Project Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Project hierarchy retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProjectTaskHierarchyResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Project not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @GetMapping("/{projectId}/tasks")
    @SecurityRequirement(name = "")
    public ResponseEntity<?> getProjectTaskHierarchy(
            @Parameter(description = "Project ID to get task hierarchy for", required = true, example = "ABC123")
            @PathVariable String projectId) {

        ProjectTaskHierarchyResponse response = projectService.getProjectTaskHierarchy(projectId);
        logger.info("Retrieved task hierarchy for project: {} with {} total tasks", 
                   projectId, response.getProject().getTotalTasks());
        return ResponseEntity.ok(response);
    }
}
