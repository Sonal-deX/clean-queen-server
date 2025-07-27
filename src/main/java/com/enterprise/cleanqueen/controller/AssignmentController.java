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

import com.enterprise.cleanqueen.dto.assignment.AssignCustomerRequest;
import com.enterprise.cleanqueen.dto.assignment.AssignSupervisorRequest;
import com.enterprise.cleanqueen.dto.assignment.AssignmentResponse;
import com.enterprise.cleanqueen.dto.assignment.SupervisorExitRequest;
import com.enterprise.cleanqueen.dto.assignment.SupervisorExitResponse;
import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.service.AssignmentService;

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
@RequestMapping("/assignments")
@Tag(name = "👥 Project Assignment", description = "Code-based project assignment system")
@SecurityRequirement(name = "JWT Authentication")
public class AssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @Autowired
    private AssignmentService assignmentService;

    @Operation(
            summary = "Customer Self-Assignment",
            description = """
        **Customer assigns themselves to a project using project code.**
        
        **Customer Only Access:**
        - Only verified customers can assign to projects
        - One customer per project (replaces existing assignment)
        - Uses unique project code shared by admin
        
        **Process:**
        1. Admin shares project code with customer
        2. Customer uses code to self-assign
        3. Project status updated to IN_PROGRESS
        4. Customer can now view project details and tasks
        
        **Rules:**
        - Project code must be valid and exist
        - Only one customer per project allowed
        - Previous customer assignment gets replaced
        """,
            tags = {"Project Assignment"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Customer assigned successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AssignmentResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Assignment failed - Invalid project code or project not available",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> assignCustomer(
            @Parameter(description = "Project assignment details", required = true)
            @Valid @RequestBody AssignCustomerRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AssignmentResponse response = assignmentService.assignCustomerToProject(request, userDetails.getUsername());
        logger.info("Customer assigned to project: {}", response.getProjectCode());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supervisor Self-Assignment",
            description = """
        **Supervisor assigns themselves to a project using project code.**
        
        **Supervisor Only Access:**
        - Only verified supervisors can assign to projects
        - One supervisor per project at a time
        - Can replace existing supervisor assignment
        
        **Process:**
        1. Admin shares project code with supervisor
        2. Supervisor uses code to self-assign
        3. Previous supervisor (if any) gets automatically unassigned
        4. Supervisor can now review tasks and create reviews
        
        **Dynamic Assignment:**
        - Supervisors can exit projects anytime
        - New supervisors can assign to projects
        - No project downtime during supervisor changes
        """,
            tags = {"Project Assignment"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Supervisor assigned successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AssignmentResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Assignment failed - Invalid project code or already assigned",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping("/supervisor")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> assignSupervisor(
            @Parameter(description = "Project assignment details", required = true)
            @Valid @RequestBody AssignSupervisorRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AssignmentResponse response = assignmentService.assignSupervisorToProject(request, userDetails.getUsername());
        logger.info("Supervisor assigned to project: {}", response.getProjectCode());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supervisor Exit Project",
            description = """
        **Supervisor exits from an assigned project.**
        
        **Supervisor Only Access:**
        - Only assigned supervisor can exit from project
        - Project becomes available for new supervisor assignment
        - Does not affect customer assignment or project status
        
        **Use Cases:**
        - Supervisor workload management
        - Supervisor unavailability
        - Project reassignment to different supervisor
        
        **Process:**
        1. Supervisor provides project ID
        2. System validates supervisor is assigned to project
        3. Supervisor assignment removed
        4. Project ready for new supervisor assignment
        """,
            tags = {"Project Assignment"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Supervisor exited successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SupervisorExitResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Exit failed - Not assigned to project or invalid project",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping("/supervisor/exit")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<?> supervisorExit(
            @Parameter(description = "Project exit details", required = true)
            @Valid @RequestBody SupervisorExitRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SupervisorExitResponse response = assignmentService.supervisorExitProject(request, userDetails.getUsername());
        logger.info("Supervisor exited from project: {}", response.getProjectId());
        return ResponseEntity.ok(response);
    }
}
