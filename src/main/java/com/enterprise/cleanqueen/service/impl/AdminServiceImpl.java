package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeRequest;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeResponse;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.enums.Role;
import com.enterprise.cleanqueen.repository.ProjectRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.AdminService;
import com.enterprise.cleanqueen.service.EmailService;
import com.enterprise.cleanqueen.util.CodeGenerator;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodeGenerator codeGenerator;

    @Override
    public CreateSupervisorResponse createSupervisor(CreateSupervisorRequest request) {
        // Validate if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Generate temporary password
        String temporaryPassword = codeGenerator.generateTemporaryPassword();

        // Create supervisor user
        User supervisor = new User();
        supervisor.setId(codeGenerator.generateUserId());
        supervisor.setUsername(request.getUsername());
        supervisor.setEmail(request.getEmail());
        supervisor.setPassword(passwordEncoder.encode(temporaryPassword));
        supervisor.setPhoneNumber(request.getPhoneNumber());
        supervisor.setRole(Role.SUPERVISOR);
        supervisor.setIsActive(true);
        supervisor.setIsVerified(true); // Supervisors are auto-verified by admin

        // Save supervisor
        userRepository.save(supervisor);

        // Send supervisor password email
        emailService.sendSupervisorPasswordEmail(request.getEmail(), temporaryPassword);

        logger.info("Supervisor created successfully: {}", supervisor.getEmail());

        return new CreateSupervisorResponse(
                true,
                "Supervisor account created successfully. Temporary password sent to email.",
                supervisor.getId(),
                supervisor.getEmail(),
                supervisor.getUsername()
        );
    }

    @Override
    public SendProjectCodeResponse sendProjectCode(SendProjectCodeRequest request) {
        // Find project by code
        Project project = projectRepository.findByProjectCode(request.getProjectCode())
                .orElseThrow(() -> new RuntimeException("Invalid project code"));

        // Send project code email
        emailService.sendProjectCodeEmail(request.getEmail(), request.getProjectCode(), project.getName());

        logger.info("Project code {} sent to {}", request.getProjectCode(), request.getEmail());

        return new SendProjectCodeResponse(
                true,
                "Project code sent successfully to customer email",
                request.getEmail(),
                request.getProjectCode(),
                project.getName()
        );
    }
}
