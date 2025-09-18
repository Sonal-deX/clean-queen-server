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
    
    @Schema(description = "User's first name", example = "John", required = true)
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;
    
    @Schema(description = "User's last name", example = "Doe", required = false)
    @Size(max = 50, message = "Last name must be maximum 50 characters")
    private String lastName;
    
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
    
    public RegisterRequest(String firstName, String lastName, String email, String password, String phoneNumber, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}