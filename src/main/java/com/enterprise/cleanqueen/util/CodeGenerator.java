package com.enterprise.cleanqueen.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CodeGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a 6-character alphanumeric ID for User, Project, Review
     */
    public String generateUserId() {
        return generateRandomString(6, ALPHANUMERIC);
    }

    /**
     * Generates a 10-character alphanumeric ID for CleaningRequest
     */
    public String generateCleaningRequestId() {
        return generateRandomString(10, ALPHANUMERIC);
    }

    /**
     * Generates an 8-character alphanumeric ID for Task
     */
    public String generateTaskId() {
        return generateRandomString(8, ALPHANUMERIC);
    }

    /**
     * Generates a 6-character alphanumeric ID for Review
     */
    public String generateReviewId() {
        return generateRandomString(6, ALPHANUMERIC);
    }

    /**
     * Generates a 7-character project code in XXX-XXX format
     */
    public String generateProjectCode() {
        String part1 = generateRandomString(3, ALPHANUMERIC);
        String part2 = generateRandomString(3, ALPHANUMERIC);
        return part1 + "-" + part2;
    }

    /**
     * Generates a 6-digit OTP
     */
    public String generateOTP() {
        return generateRandomString(6, NUMBERS);
    }

    /**
     * Generates a temporary password for supervisors
     */
    public String generateTemporaryPassword() {
        return generateRandomString(12, ALPHANUMERIC);
    }

    private String generateRandomString(int length, String characters) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}