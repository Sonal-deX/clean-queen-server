package com.enterprise.cleanqueen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeRequest;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllSupervisorsResponse;
import com.enterprise.cleanqueen.dto.admin.DeleteTaskResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllCustomersResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllCleaningRequestsResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllProjectsResponse;
import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.service.AdminService;

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
@RequestMapping("/admin")
@Tag(name = "🔐 Admin Management", description = "Admin-only endpoints for system management")
@SecurityRequirement(name = "JWT Authentication")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(
            summary = "Create Supervisor Account",
            description = """
        **Create a new supervisor account with temporary password.**
        
        **Admin Only Access:**
        - Only users with ADMIN role can create supervisors
        - Requires valid JWT token in Authorization header
        
        **Process:**
        1. Admin provides supervisor details
        2. System generates temporary password
        3. Email sent to supervisor with login credentials
        4. Supervisor must change password on first login
        """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Supervisor created successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CreateSupervisorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Creation failed - validation errors or email already exists",
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
                description = "❌ Access denied - Admin role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping("/supervisors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSupervisor(
            @Parameter(description = "Supervisor creation details", required = true)
            @Valid @RequestBody CreateSupervisorRequest request) {
        CreateSupervisorResponse response = adminService.createSupervisor(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Send Project Code to Customer",
            description = """
    **Send project assignment code to customer via email.**
    
    **Admin Only Access:**
    - Validates project code exists
    - Sends professional email with project details
    - Customer can use code for self-assignment
    
    **Email Contents:**
    - Project code for assignment
    - Project name and basic details
    - Instructions for self-assignment
    """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Project code sent successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SendProjectCodeResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Send failed - Invalid project code or email",
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
                description = "❌ Access denied - Admin role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping("/send-project-code")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sendProjectCode(
            @Parameter(description = "Project code email details", required = true)
            @Valid @RequestBody SendProjectCodeRequest request) {

        SendProjectCodeResponse response = adminService.sendProjectCode(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Supervisor Accounts",
            description = """
        **Retrieve all supervisor accounts in the system.**
        
        **Admin Only Access:**
        - Only users with ADMIN role can view all supervisors
        - Requires valid JWT token in Authorization header
        
        **Response:**
        - Returns list of all supervisor accounts with basic information
        - Includes account status (active/inactive)
        - Excludes sensitive information like passwords
        """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Supervisors retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = GetAllSupervisorsResponse.class)
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
    @GetMapping("/supervisors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSupervisors() {
        GetAllSupervisorsResponse response = adminService.getAllSupervisors();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Customer Accounts",
            description = """
        **Retrieve all customer accounts in the system.**
        
        **Admin Only Access:**
        - Only users with ADMIN role can view all customers
        - Requires valid JWT token in Authorization header
        
        **Response:**
        - Returns list of all customer accounts with basic information
        - Includes account status (active/inactive)
        - Excludes sensitive information like passwords
        """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Customers retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = GetAllCustomersResponse.class)
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
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCustomers() {
        GetAllCustomersResponse response = adminService.getAllCustomers();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Cleaning Requests",
            description = """
        **Retrieve all cleaning requests in the system.**
        
        **Admin Only Access:**
        - Only users with ADMIN role can view all cleaning requests
        - Requires valid JWT token in Authorization header
        
        **Response:**
        - Returns list of all cleaning requests with details
        - Includes request status and customer information
        - Ordered by creation date (newest first)
        """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Cleaning requests retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = GetAllCleaningRequestsResponse.class)
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
    @GetMapping("/cleaning-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCleaningRequests() {
        GetAllCleaningRequestsResponse response = adminService.getAllCleaningRequests();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Task (Soft Delete)",
            description = """
        **Soft delete a task by setting its status to inactive.**
        
        **Admin Only Access:**
        - Only users with ADMIN role can delete tasks
        - Requires valid JWT token in Authorization header
        
        **Important:**
        - This is a soft delete operation (sets status to 0/inactive)
        - Task data is preserved for audit purposes
        - Task will not appear in active listings
        """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Task deleted successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = DeleteTaskResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Task not found",
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
                description = "❌ Access denied - Admin role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(
            @Parameter(description = "Task ID to delete", required = true)
            @PathVariable String taskId) {
        DeleteTaskResponse response = adminService.deleteTask(taskId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Projects",
            description = """
        **Retrieve all projects in the system with detailed information.**
        
        **Admin Only Access:**
        - Only users with ADMIN role can access this endpoint
        - Returns comprehensive project information including customer and supervisor details
        - Includes task statistics and project progress information
        
        **Project Information Includes:**
        - Basic project details (ID, code, name, description, status)
        - Project specifications (due date, address, number of cleaners)
        - Customer and supervisor information (IDs and names)
        - Task statistics (total tasks, completed tasks)
        - Project rating and timestamps
        
        **Use Cases:**
        - System monitoring and overview
        - Project management dashboard
        - Administrative reporting
        - Performance analysis
        """,
            tags = {"Admin Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ All projects retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = GetAllProjectsResponse.class)
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
    @GetMapping("/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllProjects() {
        GetAllProjectsResponse response = adminService.getAllProjects();
        return ResponseEntity.ok(response);
    }
}
