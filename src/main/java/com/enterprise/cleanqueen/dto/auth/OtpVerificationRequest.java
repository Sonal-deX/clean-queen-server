// OtpVerificationRequest.java
package com.enterprise.cleanqueen.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "OTP verification request")
public class OtpVerificationRequest {
    
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Schema(description = "6-digit OTP code sent to email", example = "123456", required = true)
    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be exactly 6 digits")
    private String otp;

    // Constructors
    public OtpVerificationRequest() {}
    
    public OtpVerificationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
