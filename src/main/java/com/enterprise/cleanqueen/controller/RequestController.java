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

import com.enterprise.cleanqueen.dto.common.ErrorResponse;
import com.enterprise.cleanqueen.dto.request.CreateRequestRequest;
import com.enterprise.cleanqueen.dto.request.CreateRequestResponse;
import com.enterprise.cleanqueen.service.RequestService;

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
@RequestMapping("/requests")
@Tag(name = "🧹 Cleaning Requests", description = "Customer cleaning request management")
@SecurityRequirement(name = "JWT Authentication")
public class RequestController {

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestService requestService;

    @Operation(
            summary = "Submit Cleaning Request",
            description = """
        **Submit a new cleaning service request.**
        
        **Customer Only Access:**
        - Only verified customers can submit requests
        - Request is automatically linked to authenticated user
        - Email taken from JWT token, name/phone from request body
        
        **Process:**
        1. Customer provides cleaning details
        2. System creates request with PENDING status
        3. Request assigned unique ID for tracking
        4. Admin can later view and process request
        """,
            tags = {"Cleaning Requests"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Request submitted successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CreateRequestResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "❌ Access denied - Customer role required",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
    })
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CreateRequestResponse> createRequest(
            @Parameter(description = "Cleaning request details", required = true)
            @Valid @RequestBody CreateRequestRequest request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CreateRequestResponse response = requestService.createRequest(request, userDetails.getUsername());
        logger.info("Cleaning request created: {}", response.getRequestId());
        return ResponseEntity.ok(response);
    }
}
