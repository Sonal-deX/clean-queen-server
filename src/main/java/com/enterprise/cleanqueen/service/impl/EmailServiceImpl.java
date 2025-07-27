package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.enterprise.cleanqueen.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Clean Queen - Email Verification OTP");
            message.setText(buildOtpEmailContent(otp));

            mailSender.send(message);
            logger.info("OTP email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send OTP email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    @Override
    public void sendSupervisorPasswordEmail(String toEmail, String temporaryPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Clean Queen - Supervisor Account Created");
            message.setText(buildSupervisorPasswordEmailContent(temporaryPassword));

            mailSender.send(message);
            logger.info("Temporary password email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send temporary password email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send temporary password email", e);
        }
    }

    @Override
    public void sendRequestConfirmationEmail(String toEmail, String requestId, String customerName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Clean Queen - Request Submitted Successfully");
            message.setText(buildRequestConfirmationEmailContent(requestId, customerName));

            mailSender.send(message);
            logger.info("Request confirmation email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send request confirmation email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send confirmation email", e);
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
        return String.format(
                "Dear %s,\n\n"
                + "Thank you for choosing Clean Queen services!\n\n"
                + "Your cleaning request has been submitted successfully.\n\n"
                + "Request Details:\n"
                + "Request ID: %s\n"
                + "Status: Pending Review\n\n"
                + "Our team will review your request and contact you within 24 hours to discuss the details and schedule your cleaning service.\n\n"
                + "For any questions, please contact us or reference your Request ID.\n\n"
                + "Best regards,\n"
                + "Clean Queen Customer Service Team",
                customerName, requestId
        );
    }
}
