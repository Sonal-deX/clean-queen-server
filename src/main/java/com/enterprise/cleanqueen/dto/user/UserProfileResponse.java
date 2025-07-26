package com.enterprise.cleanqueen.dto.user;

import java.time.LocalDateTime;

import com.enterprise.cleanqueen.enums.Role;

public class UserProfileResponse {
    
    private String userId;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private boolean isActive;
    private boolean isVerified;
    private LocalDateTime createdAt;
    
    // Constructors
    public UserProfileResponse() {}
    
    public UserProfileResponse(String userId, String username, String email, String phoneNumber, 
                              Role role, boolean isActive, boolean isVerified, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isActive = isActive;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}