package com.enterprise.cleanqueen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeRequest;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeResponse;
import com.enterprise.cleanqueen.dto.common.ErrorResponse;
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
                        schema = @Schema(implementation = ErrorResponse.class)
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
    @PostMapping("/supervisors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSupervisor(
            @Parameter(description = "Supervisor creation details", required = true)
            @Valid @RequestBody CreateSupervisorRequest request) {
        try {
            CreateSupervisorResponse response = adminService.createSupervisor(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
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
                        schema = @Schema(implementation = ErrorResponse.class)
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
}
