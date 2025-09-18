package com.enterprise.cleanqueen.dto.auth;

import com.enterprise.cleanqueen.enums.Role;


public class AuthResponse {
    
    
    private String token;
    
    
    private String refreshToken;
    
    
    private String userId;
    
    
    private String firstName;
    
    
    private String lastName;
    
    
    private String email;
    
    
    private Role role;
    
    
    private long expiresIn;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String token, String refreshToken, String userId, String firstName, String lastName, String email, Role role, long expiresIn) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.expiresIn = expiresIn;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
}