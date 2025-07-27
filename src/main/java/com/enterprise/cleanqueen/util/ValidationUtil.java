package com.enterprise.cleanqueen.util;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[1-9]\\d{1,14}$"
    );
    
    private static final Pattern PROJECT_CODE_PATTERN = Pattern.compile(
        "^[A-Z0-9]{3}-[A-Z0-9]{3}$"
    );
    
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(
        "^[A-Z0-9]+$"
    );
    
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public boolean isValidPhoneNumber(String phone) {
        return phone == null || phone.trim().isEmpty() || PHONE_PATTERN.matcher(phone).matches();
    }
    
    public boolean isValidProjectCode(String code) {
        return code != null && PROJECT_CODE_PATTERN.matcher(code).matches();
    }
    
    public boolean isValidAlphanumericId(String id, int expectedLength) {
        return id != null && 
               id.length() == expectedLength && 
               ALPHANUMERIC_PATTERN.matcher(id).matches();
    }
    
    public boolean isValidPassword(String password) {
        return password != null && 
               password.length() >= 6 && 
               password.length() <= 100;
    }
    
    public boolean isValidUsername(String username) {
        return username != null && 
               username.trim().length() >= 3 && 
               username.trim().length() <= 50 &&
               username.matches("^[a-zA-Z0-9_.-]+$");
    }
    
    public String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"'&]", "");
    }
}
