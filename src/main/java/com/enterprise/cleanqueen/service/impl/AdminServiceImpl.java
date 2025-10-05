package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;
import com.enterprise.cleanqueen.dto.admin.DeleteTaskResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllCleaningRequestsResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllCustomersResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllSupervisorsResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllProjectsResponse;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeRequest;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeResponse;
import com.enterprise.cleanqueen.dto.request.CleaningRequestSummaryDto;
import com.enterprise.cleanqueen.dto.user.UserSummaryDto;
import com.enterprise.cleanqueen.entity.CleaningRequest;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.enums.Role;
import com.enterprise.cleanqueen.repository.CleaningRequestRepository;
import com.enterprise.cleanqueen.repository.ProjectRepository;
import com.enterprise.cleanqueen.repository.TaskRepository;
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
    private TaskRepository taskRepository;

    @Autowired
    private CleaningRequestRepository cleaningRequestRepository;

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

        // Generate temporary password
        String temporaryPassword = codeGenerator.generateTemporaryPassword();

        // Create supervisor user
        User supervisor = new User();
        supervisor.setId(codeGenerator.generateUserId());
        supervisor.setFirstName(request.getFirstName());
        supervisor.setLastName(request.getLastName());
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
                supervisor.getFirstName(),
                supervisor.getLastName()
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

    @Override
    public GetAllSupervisorsResponse getAllSupervisors() {
        try {
            List<User> supervisors = userRepository.findByRole(Role.SUPERVISOR);
            
            List<UserSummaryDto> supervisorDtos = supervisors.stream()
                    .map(this::convertToUserSummaryDto)
                    .collect(Collectors.toList());
            
            logger.info("Retrieved {} supervisors", supervisorDtos.size());
            
            return new GetAllSupervisorsResponse(
                    true,
                    "Supervisors retrieved successfully",
                    supervisorDtos
            );
        } catch (Exception e) {
            logger.error("Error retrieving supervisors: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve supervisors");
        }
    }

    @Override
    public GetAllCustomersResponse getAllCustomers() {
        try {
            List<User> customers = userRepository.findByRole(Role.CUSTOMER);
            
            List<UserSummaryDto> customerDtos = customers.stream()
                    .map(this::convertToUserSummaryDto)
                    .collect(Collectors.toList());
            
            logger.info("Retrieved {} customers", customerDtos.size());
            
            return new GetAllCustomersResponse(
                    true,
                    "Customers retrieved successfully",
                    customerDtos
            );
        } catch (Exception e) {
            logger.error("Error retrieving customers: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve customers");
        }
    }

    @Override
    public GetAllCleaningRequestsResponse getAllCleaningRequests() {
        try {
            List<CleaningRequest> cleaningRequests = cleaningRequestRepository.findAllByOrderByCreatedAtDesc();
            
            List<CleaningRequestSummaryDto> requestDtos = cleaningRequests.stream()
                    .map(this::convertToCleaningRequestSummaryDto)
                    .collect(Collectors.toList());
            
            logger.info("Retrieved {} cleaning requests", requestDtos.size());
            
            return new GetAllCleaningRequestsResponse(
                    true,
                    "Cleaning requests retrieved successfully",
                    requestDtos
            );
        } catch (Exception e) {
            logger.error("Error retrieving cleaning requests: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve cleaning requests");
        }
    }

    @Override
    public DeleteTaskResponse deleteTask(String taskId) {
        try {
            Optional<Task> taskOptional = taskRepository.findById(taskId);
            
            if (taskOptional.isEmpty()) {
                throw new RuntimeException("Task not found with ID: " + taskId);
            }
            
            Task task = taskOptional.get();
            
            // Soft delete by setting isActive to false (status 0 = inactive)
            task.setIsActive(false);
            taskRepository.save(task);
            
            logger.info("Task {} soft deleted successfully", taskId);
            
            return new DeleteTaskResponse(
                    true,
                    "Task deleted successfully (soft delete)",
                    task.getId(),
                    task.getName()
            );
        } catch (Exception e) {
            logger.error("Error deleting task {}: {}", taskId, e.getMessage());
            throw new RuntimeException("Failed to delete task: " + e.getMessage());
        }
    }
    
    // Helper methods
    private UserSummaryDto convertToUserSummaryDto(User user) {
        return new UserSummaryDto(
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
    
    private CleaningRequestSummaryDto convertToCleaningRequestSummaryDto(CleaningRequest request) {
        return new CleaningRequestSummaryDto(
                request.getId(),
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getServiceAddress(),
                request.getServiceType(),
                request.getPreferredTime(),
                request.getAdditionalDetails(),
                request.getStatus(),
                request.getUserId(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

    @Override
    public GetAllProjectsResponse getAllProjects() {
        try {
            // Get all projects
            List<Project> allProjects = projectRepository.findAll();
            
            // Convert to ProjectInfo DTOs
            List<GetAllProjectsResponse.ProjectInfo> projectInfos = allProjects.stream()
                .map(this::convertToProjectInfo)
                .collect(Collectors.toList());
            
            String message = String.format("Retrieved %d projects successfully", projectInfos.size());
            logger.info(message);
            
            return new GetAllProjectsResponse(true, message, projectInfos);
            
        } catch (Exception e) {
            logger.error("Error retrieving all projects: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve all projects", e);
        }
    }

    private GetAllProjectsResponse.ProjectInfo convertToProjectInfo(Project project) {
        // Get customer information
        String customerName = null;
        Optional<User> customer = userRepository.findById(project.getCustomerId());
        if (customer.isPresent()) {
            User c = customer.get();
            customerName = c.getFirstName() + (c.getLastName() != null ? " " + c.getLastName() : "");
        }
        
        // Get supervisor information
        String supervisorName = null;
        if (project.getSupervisorId() != null) {
            Optional<User> supervisor = userRepository.findById(project.getSupervisorId());
            if (supervisor.isPresent()) {
                User s = supervisor.get();
                supervisorName = s.getFirstName() + (s.getLastName() != null ? " " + s.getLastName() : "");
            }
        }
        
        // Get task statistics
        List<Task> projectTasks = taskRepository.findByProjectId(project.getId());
        int totalTasks = projectTasks.size();
        int completedTasks = (int) projectTasks.stream()
            .filter(task -> task.getStatus() == com.enterprise.cleanqueen.enums.TaskStatus.COMPLETED)
            .count();
        
        return new GetAllProjectsResponse.ProjectInfo(
            project.getId(),
            project.getProjectCode(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getDueDate(),
            project.getAddress(),
            project.getNoOfCleaners(),
            project.getCustomerId(),
            customerName,
            project.getSupervisorId(),
            supervisorName,
            totalTasks,
            completedTasks,
            project.getAverageRating(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
