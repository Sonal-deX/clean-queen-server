package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.auth.AuthRequest;
import com.enterprise.cleanqueen.dto.auth.AuthResponse;
import com.enterprise.cleanqueen.dto.auth.OtpVerificationRequest;
import com.enterprise.cleanqueen.dto.auth.RegisterRequest;

public interface AuthService {
    
    void register(RegisterRequest request);
    
    AuthResponse verifyOtpAndActivateAccount(OtpVerificationRequest request);
    
    AuthResponse authenticate(AuthRequest request);
    
    void resendOtp(String email);
}