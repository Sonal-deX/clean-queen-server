// RegisterRequest.java
package com.enterprise.cleanqueen.dto.auth;

import com.enterprise.cleanqueen.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "User registration request")
@JsonIgnoreProperties(ignoreUnknown = false) // Strict: reject unknown properties
public class RegisterRequest {
    
    @Schema(description = "Username for the account", example = "john_doe", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Schema(description = "Password for the account", example = "SecurePassword123", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    @Schema(description = "User's phone number", example = "+1234567890", required = false)
    private String phoneNumber;
    
    @Schema(description = "User role in the system", example = "CUSTOMER", required = true)
    @NotNull(message = "Role is required")
    private Role role;

    // Constructors
    public RegisterRequest() {}
    
    public RegisterRequest(String username, String email, String password, String phoneNumber, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}