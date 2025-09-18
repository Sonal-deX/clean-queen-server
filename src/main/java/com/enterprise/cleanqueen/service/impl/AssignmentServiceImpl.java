package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.assignment.AssignCustomerRequest;
import com.enterprise.cleanqueen.dto.assignment.AssignSupervisorRequest;
import com.enterprise.cleanqueen.dto.assignment.AssignmentResponse;
import com.enterprise.cleanqueen.dto.assignment.SupervisorExitRequest;
import com.enterprise.cleanqueen.dto.assignment.SupervisorExitResponse;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.enums.ProjectStatus;
import com.enterprise.cleanqueen.repository.ProjectRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.AssignmentService;

@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AssignmentResponse assignCustomerToProject(AssignCustomerRequest request, String userEmail) {
        // Find user
        User customer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Find project by code
        Project project = projectRepository.findByProjectCode(request.getProjectCode())
                .orElseThrow(() -> new RuntimeException("Invalid project code"));

        // Check if project is available for assignment
        if (project.getStatus() == ProjectStatus.COMPLETED || project.getStatus() == ProjectStatus.CANCELLED) {
            throw new RuntimeException("Project is not available for assignment");
        }

        // Assign customer (replace existing if any)
        project.setCustomerId(customer.getId());

        // Update project status to IN_PROGRESS if it was PENDING_ASSIGNMENT
        if (project.getStatus() == ProjectStatus.PENDING_ASSIGNMENT) {
            project.setStatus(ProjectStatus.IN_PROGRESS);
        }

        projectRepository.save(project);

        logger.info("Customer {} assigned to project {}", customer.getEmail(), project.getProjectCode());

        return new AssignmentResponse(
                true,
                "Successfully assigned to project. You can now view project details and track progress.",
                project.getId(),
                project.getProjectCode(),
                project.getName(),
                "CUSTOMER"
        );
    }

    @Override
    public AssignmentResponse assignSupervisorToProject(AssignSupervisorRequest request, String userEmail) {
        // Find user
        User supervisor = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        // Find project by code
        Project project = projectRepository.findByProjectCode(request.getProjectCode())
                .orElseThrow(() -> new RuntimeException("Invalid project code"));

        // Check if project is available for assignment
        if (project.getStatus() == ProjectStatus.COMPLETED || project.getStatus() == ProjectStatus.CANCELLED) {
            throw new RuntimeException("Project is not available for assignment");
        }

        // Check if supervisor is already assigned to this project
        if (supervisor.getId().equals(project.getSupervisorId())) {
            throw new RuntimeException("You are already assigned to this project");
        }

        // Assign supervisor (replace existing if any)
        project.setSupervisorId(supervisor.getId());
        projectRepository.save(project);

        logger.info("Supervisor {} assigned to project {}", supervisor.getEmail(), project.getProjectCode());

        return new AssignmentResponse(
                true,
                "Successfully assigned as supervisor. You can now review tasks and manage project progress.",
                project.getId(),
                project.getProjectCode(),
                project.getName(),
                "SUPERVISOR"
        );
    }

    @Override
    public SupervisorExitResponse supervisorExitProject(SupervisorExitRequest request, String userEmail) {
        // Find user
        User supervisor = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        // Find project
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if supervisor is actually assigned to this project
        if (!supervisor.getId().equals(project.getSupervisorId())) {
            throw new RuntimeException("You are not assigned to this project");
        }

        // Remove supervisor assignment
        project.setSupervisorId(null);
        projectRepository.save(project);

        logger.info("Supervisor {} exited from project {}", supervisor.getEmail(), project.getId());

        return new SupervisorExitResponse(
                true,
                "Successfully exited from project. Project is now available for new supervisor assignment.",
                project.getId(),
                project.getName()
        );
    }
}
