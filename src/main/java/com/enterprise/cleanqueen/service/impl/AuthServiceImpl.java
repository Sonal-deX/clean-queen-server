package com.enterprise.cleanqueen.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.auth.AuthRequest;
import com.enterprise.cleanqueen.dto.auth.AuthResponse;
import com.enterprise.cleanqueen.dto.auth.OtpVerificationRequest;
import com.enterprise.cleanqueen.dto.auth.RefreshTokenRequest;
import com.enterprise.cleanqueen.dto.auth.RefreshTokenResponse;
import com.enterprise.cleanqueen.dto.auth.RegisterRequest;
import com.enterprise.cleanqueen.entity.OtpVerification;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.enums.Role;
import com.enterprise.cleanqueen.repository.OtpVerificationRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.AuthService;
import com.enterprise.cleanqueen.service.EmailService;
import com.enterprise.cleanqueen.service.JwtService;
import com.enterprise.cleanqueen.util.CodeGenerator;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpVerificationRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.otp.expiration-in-minutes}")
    private int otpExpirationMinutes;

    @Override
    public void register(RegisterRequest request) {
        // Check for existing verified user
        if (userRepository.existsByEmail(request.getEmail())) {
            User existingUser = userRepository.findByEmail(request.getEmail()).get();

            // If user is verified, don't allow re-registration
            if (existingUser.getIsVerified()) {
                throw new RuntimeException("Email is already registered and verified");
            }

            // If user is not verified, check if OTP is still valid
            boolean hasValidOtp = otpRepository
                    .findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(request.getEmail())
                    .map(otp -> !otp.isExpired())
                    .orElse(false);

            if (hasValidOtp) {
                throw new RuntimeException("Registration pending. Please verify your email with the OTP sent, or wait for it to expire before re-registering.");
            }

            // OTP expired and user not verified - allow re-registration
            // Clean up old unverified user and their OTPs
            otpRepository.deleteByEmail(request.getEmail());
            userRepository.delete(existingUser);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Only allow CUSTOMER and ADMIN registration (SUPERVISOR created by admin only)
        if (request.getRole() == Role.SUPERVISOR) {
            throw new RuntimeException("Supervisors can only be created by admin");
        }

        // Create user entity
        User user = new User();
        user.setId(codeGenerator.generateUserId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setIsActive(true);
        user.setIsVerified(false); // Will be verified after OTP confirmation

        // Save user
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());

        // Generate and send OTP
        sendOtpToUser(request.getEmail(), request.getRole());
    }

    @Override
    public AuthResponse verifyOtpAndActivateAccount(OtpVerificationRequest request) {
        // Find the latest unused OTP for this email
        OtpVerification otpVerification = otpRepository
                .findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        // Check if OTP matches and is not expired
        if (!otpVerification.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (otpVerification.isExpired()) {
            throw new RuntimeException("OTP has expired");
        }

        // Find and activate user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsVerified(true);
        userRepository.save(user);

        // Mark OTP as used
        otpVerification.setIsUsed(true);
        otpRepository.save(otpVerification);

        logger.info("User account verified and activated: {}", user.getEmail());

        // Generate JWT tokens and return response
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
                token,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                jwtService.getJwtExpiration()
        );
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check if user is verified
        if (!user.getIsVerified()) {
            throw new RuntimeException("Account not verified. Please verify your email first.");
        }

        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated. Please contact admin.");
        }

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Generate tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        logger.info("User authenticated successfully: {}", user.getEmail());

        return new AuthResponse(
                token,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                jwtService.getJwtExpiration()
        );
    }

    @Override
    public void resendOtp(String email) {
        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is already verified
        if (user.getIsVerified()) {
            throw new RuntimeException("Account is already verified");
        }

        // Mark all previous OTPs as used
        otpRepository.markAllAsUsedByEmail(email);

        // Generate and send new OTP
        sendOtpToUser(email, user.getRole());

        logger.info("OTP resent successfully to: {}", email);
    }

    private void sendOtpToUser(String email, Role role) {
        // Generate OTP
        String otp = codeGenerator.generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(otpExpirationMinutes);

        // Save OTP
        OtpVerification otpVerification = new OtpVerification(email, otp, expiresAt);
        otpRepository.save(otpVerification);

        // Send OTP to appropriate email
        String targetEmail = (role == Role.ADMIN) ? adminEmail : email;
        emailService.sendOtpEmail(targetEmail, otp);

        logger.info("OTP generated and sent to: {} (original: {})", targetEmail, email);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Extract username from refresh token
        String userEmail = jwtService.extractUsername(refreshToken);

        // Load user details
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is active and verified
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        if (!user.getIsVerified()) {
            throw new RuntimeException("Account is not verified");
        }

        // Load user details for token validation
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // Validate refresh token
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        // Generate new tokens
        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        logger.info("Tokens refreshed successfully for user: {}", userEmail);

        return new RefreshTokenResponse(
                true,
                "Tokens refreshed successfully",
                newAccessToken,
                newRefreshToken,
                jwtService.getJwtExpiration()
        );
    }
}
