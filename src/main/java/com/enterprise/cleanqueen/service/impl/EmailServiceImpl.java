package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.enterprise.cleanqueen.exception.BusinessException;
import com.enterprise.cleanqueen.service.EmailService;
import com.enterprise.cleanqueen.util.ValidationUtil;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ValidationUtil validationUtil;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async("taskExecutor")
    public void sendOtpEmail(String toEmail, String otp) {
        // Validate email format
        if (!validationUtil.isValidEmail(toEmail)) {
            logger.error("Invalid email format: {}", toEmail);
            throw new BusinessException("Invalid email format");
        }

        // Sanitize inputs for security
        String cleanEmail = validationUtil.sanitizeInput(toEmail);
        String cleanOtp = validationUtil.sanitizeInput(otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(cleanEmail);
            message.setSubject("Clean Queen - Email Verification OTP");
            message.setText(buildOtpEmailContent(cleanOtp));

            mailSender.send(message);
            logger.info("OTP email sent successfully to: {}", cleanEmail);
        } catch (MailException e) {
            logger.error("Failed to send OTP email to: {}", cleanEmail, e);
            throw new BusinessException("Failed to send OTP email: " + e.getMessage());
        }
    }

    @Override
    @Async("taskExecutor")
    public void sendSupervisorPasswordEmail(String toEmail, String temporaryPassword) {
        // Validate email format
        if (!validationUtil.isValidEmail(toEmail)) {
            logger.error("Invalid email format: {}", toEmail);
            throw new BusinessException("Invalid email format");
        }

        // Sanitize inputs for security
        String cleanEmail = validationUtil.sanitizeInput(toEmail);
        String cleanPassword = validationUtil.sanitizeInput(temporaryPassword);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(cleanEmail);
            message.setSubject("Clean Queen - Supervisor Account Created");
            message.setText(buildSupervisorPasswordEmailContent(cleanPassword));

            mailSender.send(message);
            logger.info("Temporary password email sent successfully to: {}", cleanEmail);
        } catch (MailException e) {
            logger.error("Failed to send temporary password email to: {}", cleanEmail, e);
            throw new BusinessException("Failed to send temporary password email: " + e.getMessage());
        }
    }

    @Override
    @Async("taskExecutor")
    public void sendRequestConfirmationEmail(String toEmail, String requestId, String customerName) {
        // Validate email format
        if (!validationUtil.isValidEmail(toEmail)) {
            logger.error("Invalid email format: {}", toEmail);
            throw new BusinessException("Invalid email format");
        }

        // Sanitize inputs for security
        String cleanEmail = validationUtil.sanitizeInput(toEmail);
        String cleanRequestId = validationUtil.sanitizeInput(requestId);
        String cleanCustomerName = validationUtil.sanitizeInput(customerName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(cleanEmail);
            message.setSubject("Clean Queen - Request Submitted Successfully");
            message.setText(buildRequestConfirmationEmailContent(cleanRequestId, cleanCustomerName));

            mailSender.send(message);
            logger.info("Request confirmation email sent to: {}", cleanEmail);
        } catch (MailException e) {
            logger.error("Failed to send request confirmation email to: {}", cleanEmail, e);
            throw new BusinessException("Failed to send confirmation email: " + e.getMessage());
        }
    }

    @Override
    @Async("taskExecutor")
    public void sendProjectCodeEmail(String toEmail, String projectCode, String projectName) {
        // Validate email format
        if (!validationUtil.isValidEmail(toEmail)) {
            logger.error("Invalid email format: {}", toEmail);
            throw new BusinessException("Invalid email format");
        }

        // Validate project code format
        if (!validationUtil.isValidProjectCode(projectCode)) {
            logger.error("Invalid project code format: {}", projectCode);
            throw new BusinessException("Invalid project code format");
        }

        // Sanitize inputs for security
        String cleanEmail = validationUtil.sanitizeInput(toEmail);
        String cleanProjectCode = validationUtil.sanitizeInput(projectCode);
        String cleanProjectName = validationUtil.sanitizeInput(projectName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(cleanEmail);
            message.setSubject("Clean Queen - Project Assignment Code");
            message.setText(buildProjectCodeEmailContent(cleanProjectCode, cleanProjectName));

            mailSender.send(message);
            logger.info("Project code email sent successfully to: {}", cleanEmail);
        } catch (MailException e) {
            logger.error("Failed to send project code email to: {}", cleanEmail, e);
            throw new BusinessException("Failed to send project code email: " + e.getMessage());
        }
    }

    private String buildOtpEmailContent(String otp) {
        return String.format("""
                             Dear User,
                             
                             Thank you for registering with Clean Queen Service Management System.
                             
                             Your email verification OTP is: %s
                             
                             This OTP will expire in 5 minutes. Please use it to complete your registration.
                             
                             If you did not request this, please ignore this email.
                             
                             Best regards,
                             Clean Queen Team""",
                otp
        );
    }

    private String buildSupervisorPasswordEmailContent(String temporaryPassword) {
        return String.format("""
                             Dear Supervisor,
                             
                             Welcome to Clean Queen Service Management System!
                             
                             Your supervisor account has been created. Here are your login credentials:
                             
                             Password: %s
                             
                             Please log in and change your password immediately for security purposes.
                             
                             Login at: http://localhost:8080/api/auth/login
                             
                             Best regards,
                             Clean Queen Admin Team""",
                temporaryPassword
        );
    }

    private String buildRequestConfirmationEmailContent(String requestId, String customerName) {
        return String.format("""
                             Dear %s,
                             
                             Thank you for choosing Clean Queen services!
                             
                             Your cleaning request has been submitted successfully.
                             
                             Request Details:
                             Request ID: %s
                             Status: Pending Review
                             
                             Our team will review your request and contact you within 24 hours to discuss the details and schedule your cleaning service.
                             
                             For any questions, please contact us or reference your Request ID.
                             
                             Best regards,
                             Clean Queen Customer Service Team""",
                customerName, requestId
        );
    }

    private String buildProjectCodeEmailContent(String projectCode, String projectName) {
        return String.format("""
                             Dear Customer,
                             
                             Your cleaning project has been created and is ready for assignment.
                             
                             Project Details:
                             Project Name: %s
                             Project Code: %s
                             
                             To assign yourself to this project:
                             1. Login to the Clean Queen system
                             2. Use the project assignment feature
                             3. Enter the project code above
                             
                             Once assigned, you'll be able to track project progress and communicate with our team.
                             
                             Best regards,
                             Clean Queen Project Team""",
                projectName, projectCode
        );
    }
}
