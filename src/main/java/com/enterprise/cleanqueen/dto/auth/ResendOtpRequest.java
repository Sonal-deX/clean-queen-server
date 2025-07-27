package com.enterprise.cleanqueen.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for resending OTP
 */
@Schema(description = "Request to resend OTP for email verification")
public class ResendOtpRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address to resend OTP to", example = "user@example.com")
    private String email;

    // Default constructor
    public ResendOtpRequest() {}

    // Constructor
    public ResendOtpRequest(String email) {
        this.email = email;
    }

    // Getter and Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
