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
    public void sendTemporaryPasswordEmail(String toEmail, String temporaryPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Clean Queen - Supervisor Account Created");
            message.setText(buildTemporaryPasswordEmailContent(temporaryPassword));

            mailSender.send(message);
            logger.info("Temporary password email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send temporary password email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send temporary password email", e);
        }
    }

    @Override
    public void sendProjectCodeEmail(String toEmail, String projectCode, String projectName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Clean Queen - Project Assignment Code");
            message.setText(buildProjectCodeEmailContent(projectCode, projectName));

            mailSender.send(message);
            logger.info("Project code email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send project code email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send project code email", e);
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

    private String buildTemporaryPasswordEmailContent(String temporaryPassword) {
        return String.format("""
                             Dear Supervisor,
                             
                             Welcome to Clean Queen Service Management System!
                             
                             Your supervisor account has been created. Here are your login credentials:
                             
                             Temporary Password: %s
                             
                             Please log in and change your password immediately for security purposes.
                             
                             Login at: http://localhost:8080/api/auth/login
                             
                             Best regards,
                             Clean Queen Admin Team""",
                temporaryPassword
        );
    }

    private String buildProjectCodeEmailContent(String projectCode, String projectName) {
        return String.format("""
                             Dear Customer,
                             
                             Your cleaning project '%s' has been created and is ready for assignment.
                             
                             Project Code: %s
                             
                             Please use this code to assign yourself to the project through our system.
                             
                             Best regards,
                             Clean Queen Team""",
                projectName, projectCode
        );
    }
}
