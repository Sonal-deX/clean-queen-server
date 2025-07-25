package com.enterprise.cleanqueen.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.repository.OtpVerificationRepository;
import com.enterprise.cleanqueen.repository.UserRepository;

@Service
@Transactional
public class OtpCleanupService {
    
    private static final Logger logger = LoggerFactory.getLogger(OtpCleanupService.class);
    
    @Autowired
    private OtpVerificationRepository otpRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Clean up expired OTPs and unverified users every 10 minutes
     */
    @Scheduled(fixedRate = 600000) // 10 minutes
    public void cleanupExpiredOtpsAndUnverifiedUsers() {
        LocalDateTime now = LocalDateTime.now();
        
        try {
            // Find all unverified users whose OTPs have expired
            List<User> unverifiedUsers = userRepository.findByIsVerified(false);
            
            for (User user : unverifiedUsers) {
                boolean hasValidOtp = otpRepository
                        .findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(user.getEmail())
                        .map(otp -> !otp.isExpired())
                        .orElse(false);
                
                if (!hasValidOtp) {
                    // No valid OTP exists, remove user and their OTPs
                    otpRepository.deleteByEmail(user.getEmail());
                    userRepository.delete(user);
                    logger.info("Cleaned up unverified user with expired OTP: {}", user.getEmail());
                }
            }
            
            // Delete all expired OTPs
            otpRepository.deleteExpiredOtps(now);
            logger.debug("Cleanup completed for expired OTPs and unverified users");
            
        } catch (Exception e) {
            logger.error("Error during OTP cleanup process", e);
        }
    }
}