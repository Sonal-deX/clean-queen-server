package com.enterprise.cleanqueen.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Create cleaning request")
public class CreateRequestRequest {

    @Schema(description = "Customer name", example = "John Doe", required = true)
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Schema(description = "Contact phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Email address", example = "john.doe@email.com", required = true)
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Service address", example = "123 Main Street, City, State", required = true)
    @NotBlank(message = "Service address is required")
    @Size(max = 200, message = "Service address cannot exceed 200 characters")
    private String serviceAddress;

    @Schema(description = "Type of cleaning service", example = "Office Cleaning", required = true)
    @NotBlank(message = "Service type is required")
    @Size(max = 100, message = "Service type cannot exceed 100 characters")
    private String serviceType;

    @Schema(description = "Preferred time for service", example = "Morning (9 AM - 12 PM)")
    @Size(max = 100, message = "Preferred time cannot exceed 100 characters")
    private String preferredTime;

    @Schema(description = "Additional details or special requirements", example = "Please use eco-friendly products")
    @Size(max = 1000, message = "Additional details cannot exceed 1000 characters")
    private String additionalDetails;

    // Constructors
    public CreateRequestRequest() {
    }

    public CreateRequestRequest(String name, String phoneNumber, String email, 
                               String serviceAddress, String serviceType, 
                               String preferredTime, String additionalDetails) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.serviceAddress = serviceAddress;
        this.serviceType = serviceType;
        this.preferredTime = preferredTime;
        this.additionalDetails = additionalDetails;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}
