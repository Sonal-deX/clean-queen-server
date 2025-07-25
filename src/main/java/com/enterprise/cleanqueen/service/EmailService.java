package com.enterprise.cleanqueen.service;

public interface EmailService {
    
    void sendOtpEmail(String toEmail, String otp);
    
    void sendTemporaryPasswordEmail(String toEmail, String temporaryPassword);
    
    void sendProjectCodeEmail(String toEmail, String projectCode, String projectName);
}