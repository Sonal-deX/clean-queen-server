package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.user.UpdateProfileRequest;
import com.enterprise.cleanqueen.dto.user.UpdateProfileResponse;
import com.enterprise.cleanqueen.dto.user.UserProfileResponse;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserProfileResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getRole(),
            user.getIsActive(),
            user.getIsVerified(),
            user.getCreatedAt()
        );
    }
    
    @Override
    public UpdateProfileResponse updateUserProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        boolean hasChanges = false;
        
        // Update first name if provided
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName());
            hasChanges = true;
        }
        
        // Update last name if provided (can be empty to clear it)
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName().trim().isEmpty() ? null : request.getLastName());
            hasChanges = true;
        }
        
        // Update phone number if provided
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
            hasChanges = true;
        }
        
        // Update password if provided
        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
                throw new RuntimeException("Current password is required to change password");
            }
            
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            hasChanges = true;
        }
        
        if (!hasChanges) {
            throw new RuntimeException("No changes provided");
        }
        
        // Save updated user
        userRepository.save(user);
        
        logger.info("Profile updated successfully for user: {}", email);
        
        return new UpdateProfileResponse(
            true,
            "Profile updated successfully",
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber()
        );
    }
}