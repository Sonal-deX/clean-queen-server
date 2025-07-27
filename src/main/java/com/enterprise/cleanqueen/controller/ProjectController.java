package com.enterprise.cleanqueen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.common.ErrorResponse;
import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;
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
    public ResponseEntity<ProjectCreateResponse> createProject(
            @Parameter(description = "Project creation details with task hierarchy", required = true)
            @Valid @RequestBody ProjectCreateRequest request) {

        ProjectCreateResponse response = projectService.createProject(request);
        logger.info("Project created: {} with code: {}", response.getProjectId(), response.getProjectCode());
        return ResponseEntity.ok(response);
    }
}
