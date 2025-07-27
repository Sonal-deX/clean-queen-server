package com.enterprise.cleanqueen.dto.auth;

import java.time.LocalDateTime;

public class RefreshTokenResponse {

    private boolean success;
    private String message;
    private String token;
    private String refreshToken;
    private long expiresIn;
    private LocalDateTime timestamp;

    // Constructors
    public RefreshTokenResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public RefreshTokenResponse(boolean success, String message, String token, String refreshToken, long expiresIn) {
        this();
        this.success = success;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
