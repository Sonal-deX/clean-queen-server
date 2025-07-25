package com.enterprise.cleanqueen.util;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {
    
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String NUMERIC = "0123456789";
    private static final Random random = new SecureRandom();
    
    /**
     * Generate a 6-character alphanumeric ID for User and Project entities
     */
    public String generateUserId() {
        return generateRandomString(6, ALPHANUMERIC);
    }
    
    /**
     * Generate a 6-character alphanumeric ID for Project entities
     */
    public String generateProjectId() {
        return generateRandomString(6, ALPHANUMERIC);
    }
    
    /**
     * Generate a 10-character alphanumeric ID for CleaningRequest entities
     */
    public String generateRequestId() {
        return generateRandomString(10, ALPHANUMERIC);
    }
    
    /**
     * Generate an 8-character alphanumeric ID for Task entities
     */
    public String generateTaskId() {
        return generateRandomString(8, ALPHANUMERIC);
    }
    
    /**
     * Generate a 6-character alphanumeric ID for Review entities
     */
    public String generateReviewId() {
        return generateRandomString(6, ALPHANUMERIC);
    }
    
    /**
     * Generate a 7-character project code in XXX-XXX format
     */
    public String generateProjectCode() {
        String part1 = generateRandomString(3, ALPHANUMERIC);
        String part2 = generateRandomString(3, ALPHANUMERIC);
        return part1 + "-" + part2;
    }
    
    /**
     * Generate a 6-digit OTP for email verification
     */
    public String generateOTP() {
        return generateRandomString(6, NUMERIC);
    }
    
    /**
     * Generate a temporary password for supervisors (8 characters)
     */
    public String generateTemporaryPassword() {
        return generateRandomString(8, ALPHANUMERIC);
    }
    
    /**
     * Helper method to generate random string of specified length using given characters
     */
    private String generateRandomString(int length, String characters) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
}