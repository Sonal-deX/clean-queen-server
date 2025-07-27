package com.enterprise.cleanqueen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.dto.user.UpdateProfileRequest;
import com.enterprise.cleanqueen.dto.user.UpdateProfileResponse;
import com.enterprise.cleanqueen.dto.user.UserProfileResponse;
import com.enterprise.cleanqueen.service.UserService;
import com.enterprise.cleanqueen.util.ValidationUtil;

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
@RequestMapping("/user")
@Tag(name = "👤 User Profile", description = "User profile management endpoints")
@SecurityRequirement(name = "JWT Authentication")
public class UserController {
    
    // private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ValidationUtil validationUtil;
    
    @Operation(
        summary = "Get User Profile",
        description = """
        **Retrieve current user's profile information.**
        
        **Authentication Required:**
        - Valid JWT token in Authorization header
        - Returns profile data for the authenticated user
        """,
        tags = {"User Profile"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "✅ Profile retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "❌ Unauthorized - Invalid or missing token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserProfileResponse response = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Update User Profile",
        description = """
        **Update current user's profile information.**
        
        **Authentication Required:**
        - Valid JWT token in Authorization header
        - Users can only update their own profile
        
        **Updatable Fields:**
        - Username (must be unique)
        - Phone number
        - Password (requires current password)
        """,
        tags = {"User Profile"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "✅ Profile updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateProfileResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "❌ Update failed - validation errors or incorrect current password",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "Profile update details", required = true)
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        
        // Validate username format if provided
        if (request.getUsername() != null && !validationUtil.isValidUsername(request.getUsername())) {
            throw new RuntimeException("Username must be 3-50 characters and contain only letters, numbers, dots, hyphens, and underscores");
        }
        
        // Validate phone number format if provided
        if (request.getPhoneNumber() != null && !validationUtil.isValidPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Invalid phone number format");
        }
        
        // Validate new password format if provided
        if (request.getNewPassword() != null && !validationUtil.isValidPassword(request.getNewPassword())) {
            throw new RuntimeException("Password must be 6-100 characters long");
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UpdateProfileResponse response = userService.updateUserProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }
}