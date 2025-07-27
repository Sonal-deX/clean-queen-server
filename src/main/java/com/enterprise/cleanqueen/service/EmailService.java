package com.enterprise.cleanqueen.service;

public interface EmailService {
    
    void sendOtpEmail(String toEmail, String otp);
    
    void sendSupervisorPasswordEmail(String toEmail, String temporaryPassword);
    
    void sendRequestConfirmationEmail(String toEmail, String requestId, String customerName);
    
}