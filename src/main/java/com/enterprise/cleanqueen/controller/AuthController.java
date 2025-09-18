package com.enterprise.cleanqueen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.cleanqueen.dto.auth.AuthRequest;
import com.enterprise.cleanqueen.dto.auth.AuthResponse;
import com.enterprise.cleanqueen.dto.auth.OtpResendResponse;
import com.enterprise.cleanqueen.dto.auth.OtpVerificationRequest;
import com.enterprise.cleanqueen.dto.auth.RefreshTokenRequest;
import com.enterprise.cleanqueen.dto.auth.RefreshTokenResponse;
import com.enterprise.cleanqueen.dto.auth.RegisterRequest;
import com.enterprise.cleanqueen.dto.auth.RegisterResponse;
import com.enterprise.cleanqueen.dto.auth.ResendOtpRequest;
import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.enterprise.cleanqueen.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    // private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Register a new customer or admin user. OTP will be sent for email verification."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Registration successful",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Registration failed - validation errors or email already exists",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        RegisterResponse response = new RegisterResponse(
                true,
                "Registration successful. Please check your email for OTP verification.",
                request.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Verify OTP and activate account",
            description = "Verify the OTP sent to email and activate the user account. Returns JWT tokens upon successful verification."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OTP verification successful, returning token details",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid or expired OTP",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @Parameter(description = "OTP verification details", required = true)
            @Valid @RequestBody OtpVerificationRequest request) {
        AuthResponse authResponse = authService.verifyOtpAndActivateAccount(request);
        // Return the AuthResponse directly on success
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user with email and password. Returns JWT tokens for authenticated users."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Login successful, returning token details",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Login failed - invalid credentials or account not verified",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
        @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @Parameter(description = "User authentication details", required = true)
            @Valid @RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.authenticate(request);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Resend OTP",
            description = "Resend OTP for email verification. Can be used if the original OTP was lost or expired."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OTP resent successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OtpResendResponse.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Resend failed - user not found or already verified",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
        @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(
            @Parameter(description = "Email to resend OTP", required = true)
            @Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request.getEmail());
        return ResponseEntity.ok().body("OTP resent successfully");
    }

    @Operation(
            summary = "Refresh Access Token",
            description = """
    **Generate new access token using refresh token.**
    
    **Use Cases:**
    - Access token has expired
    - Need fresh tokens without re-login
    - Maintain user session seamlessly
    
    **Process:**
    1. Provide valid refresh token
    2. System validates refresh token
    3. New access and refresh tokens generated
    4. Old tokens become invalid
    
    **Security:**
    - Refresh token must be valid and not expired
    - User account must be active and verified
    - Generates completely new token pair
    """,
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Tokens refreshed successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = RefreshTokenResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Refresh failed - invalid or expired refresh token",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class)
                )
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @Parameter(description = "Refresh token details", required = true)
            @Valid @RequestBody RefreshTokenRequest request) {

        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
