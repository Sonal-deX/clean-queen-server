package com.enterprise.cleanqueen.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;
import com.enterprise.cleanqueen.dto.project.ProjectListResponse;
import com.enterprise.cleanqueen.dto.project.ProjectTaskHierarchyResponse;
import com.enterprise.cleanqueen.dto.project.ProjectTaskReviewsResponse;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateResponse;
import com.enterprise.cleanqueen.dto.project.TaskCreateRequest;
import com.enterprise.cleanqueen.dto.project.TaskUpdateRequest;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.Review;
import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.enums.ProjectStatus;
import com.enterprise.cleanqueen.enums.TaskPriority;
import com.enterprise.cleanqueen.enums.TaskStatus;
import com.enterprise.cleanqueen.repository.ProjectRepository;
import com.enterprise.cleanqueen.repository.ReviewRepository;
import com.enterprise.cleanqueen.repository.TaskRepository;
import com.enterprise.cleanqueen.service.ProjectService;
import com.enterprise.cleanqueen.util.CodeGenerator;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CodeGenerator codeGenerator;

    @Override
    public ProjectCreateResponse createProject(ProjectCreateRequest request) {
        // Generate unique project code
        String projectCode;
        do {
            projectCode = codeGenerator.generateProjectCode();
        } while (projectRepository.existsByProjectCode(projectCode));

        // Create project
        Project project = new Project();
        project.setId(codeGenerator.generateProjectId());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setProjectCode(projectCode);
        project.setDueDate(request.getDueDate());
        project.setAddress(request.getAddress());
        project.setNoOfCleaners(request.getNoOfCleaners());
        project.setStatus(ProjectStatus.PENDING_ASSIGNMENT);

        // Save project first
        projectRepository.save(project);

        // Create tasks with hierarchy
        int totalTasks = createTaskHierarchy(request.getTasks(), project.getId(), null);

        logger.info("Project created successfully: {} with {} tasks", project.getId(), totalTasks);

        return new ProjectCreateResponse(
                true,
                "Project created successfully with hierarchical task structure",
                project.getId(),
                project.getProjectCode(),
                project.getName(),
                totalTasks
        );
    }

    @Override
    public ProjectUpdateResponse updateProject(String projectId, ProjectUpdateRequest request) {
        // Find existing project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Update project details
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        if (request.getDueDate() != null) {
            project.setDueDate(request.getDueDate());
        }
        if (request.getAddress() != null) {
            project.setAddress(request.getAddress());
        }
        if (request.getNoOfCleaners() != null) {
            project.setNoOfCleaners(request.getNoOfCleaners());
        }

        projectRepository.save(project);

        // Get all existing tasks for this project
        List<Task> existingTasks = taskRepository.findByProjectId(projectId);
        Set<String> existingTaskIds = existingTasks.stream().map(Task::getId).collect(Collectors.toSet());

        // Track changes
        Set<String> processedTaskIds = new HashSet<>();
        AtomicInteger tasksAdded = new AtomicInteger(0);
        AtomicInteger tasksUpdated = new AtomicInteger(0);

        // Process updated task hierarchy
        updateTaskHierarchy(request.getTasks(), projectId, null, existingTaskIds, processedTaskIds, tasksAdded, tasksUpdated);

        // Remove tasks that weren't included in the update
        Set<String> tasksToRemove = new HashSet<>(existingTaskIds);
        tasksToRemove.removeAll(processedTaskIds);

        if (!tasksToRemove.isEmpty()) {
            taskRepository.deleteAllById(tasksToRemove);
        }

        // Get final task count
        int totalTasks = taskRepository.findByProjectId(projectId).size();

        logger.info("Project {} updated: {} total, {} added, {} updated, {} removed",
                projectId, totalTasks, tasksAdded.get(), tasksUpdated.get(), tasksToRemove.size());

        return new ProjectUpdateResponse(
                true,
                "Project updated successfully with task hierarchy changes",
                project.getId(),
                project.getProjectCode(),
                project.getName(),
                totalTasks,
                tasksAdded.get(),
                tasksUpdated.get(),
                tasksToRemove.size()
        );
    }

    private void updateTaskHierarchy(List<TaskUpdateRequest> taskRequests, String projectId, String parentTaskId,
            Set<String> existingTaskIds, Set<String> processedTaskIds,
            AtomicInteger tasksAdded, AtomicInteger tasksUpdated) {
        if (taskRequests == null || taskRequests.isEmpty()) {
            return;
        }

        for (TaskUpdateRequest taskRequest : taskRequests) {
            Task task;
            boolean isNewTask = taskRequest.getId() == null || !existingTaskIds.contains(taskRequest.getId());

            if (isNewTask) {
                // Create new task
                task = new Task();
                task.setId(codeGenerator.generateTaskId());
                tasksAdded.incrementAndGet();
            } else {
                // Update existing task
                task = taskRepository.findById(taskRequest.getId())
                        .orElseThrow(() -> new RuntimeException("Task not found: " + taskRequest.getId()));
                tasksUpdated.incrementAndGet();
                processedTaskIds.add(taskRequest.getId());
            }

            // Set/update task properties
            task.setName(taskRequest.getName());
            task.setDescription(taskRequest.getDescription());
            task.setStatus(taskRequest.getStatus() != null ? taskRequest.getStatus() : TaskStatus.PENDING_ASSIGNMENT);
            task.setPriority(taskRequest.getPriority() != null ? taskRequest.getPriority() : TaskPriority.MEDIUM);
            task.setProjectId(projectId);
            task.setParentId(parentTaskId);

            taskRepository.save(task);

            // Process subtasks recursively
            if (taskRequest.getSubtasks() != null && !taskRequest.getSubtasks().isEmpty()) {
                updateTaskHierarchy(taskRequest.getSubtasks(), projectId, task.getId(),
                        existingTaskIds, processedTaskIds, tasksAdded, tasksUpdated);
            }
        }
    }

    private int createTaskHierarchy(List<TaskCreateRequest> taskRequests, String projectId, String parentTaskId) {
        if (taskRequests == null || taskRequests.isEmpty()) {
            return 0;
        }

        int taskCount = 0;
        List<Task> tasksToSave = new ArrayList<>();

        for (TaskCreateRequest taskRequest : taskRequests) {
            // Create task
            Task task = new Task();
            task.setId(codeGenerator.generateTaskId());
            task.setName(taskRequest.getName());
            task.setDescription(taskRequest.getDescription());
            task.setStatus(TaskStatus.PENDING_ASSIGNMENT);
            task.setPriority(taskRequest.getPriority() != null ? taskRequest.getPriority() : com.enterprise.cleanqueen.enums.TaskPriority.MEDIUM);
            task.setProjectId(projectId);
            task.setParentId(parentTaskId);

            tasksToSave.add(task);
            taskCount++;

            // Save current batch of tasks
            taskRepository.saveAll(tasksToSave);
            tasksToSave.clear();

            // Recursively create subtasks
            if (taskRequest.getSubtasks() != null && !taskRequest.getSubtasks().isEmpty()) {
                taskCount += createTaskHierarchy(taskRequest.getSubtasks(), projectId, task.getId());
            }
        }

        return taskCount;
    }

    @Override
    public ProjectListResponse getProjectsByUserId(String userId) {
        try {
            // Find projects where user is either customer or supervisor
            List<Project> customerProjects = projectRepository.findByCustomerId(userId);
            List<Project> supervisorProjects = projectRepository.findBySupervisorId(userId);
            
            // Combine both lists and remove duplicates
            Set<Project> allProjects = new HashSet<>();
            allProjects.addAll(customerProjects);
            allProjects.addAll(supervisorProjects);
            
            // Convert to ProjectSummary list
            List<ProjectListResponse.ProjectSummary> projectSummaries = allProjects.stream()
                .map(this::convertToProjectSummary)
                .collect(Collectors.toList());
            
            String message = String.format("Retrieved %d projects for user %s", projectSummaries.size(), userId);
            logger.info(message);
            
            return new ProjectListResponse(true, message, projectSummaries);
            
        } catch (Exception e) {
            logger.error("Error retrieving projects for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to retrieve projects for user: " + userId, e);
        }
    }

    @Override
    public ProjectTaskHierarchyResponse getProjectTaskHierarchy(String projectId) {
        try {
            // Find the project
            Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
            
            // Get all root tasks (tasks with no parent)
            List<Task> rootTasks = taskRepository.findByProjectIdAndParentIdIsNull(projectId);
            
            // Build task hierarchy
            List<ProjectTaskHierarchyResponse.TaskHierarchy> taskHierarchies = rootTasks.stream()
                .map(this::buildTaskHierarchy)
                .collect(Collectors.toList());
            
            // Calculate task statistics
            List<Task> allTasks = taskRepository.findByProjectId(projectId);
            int totalTasks = allTasks.size();
            int completedTasks = (int) allTasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();
            
            // Create project details
            ProjectTaskHierarchyResponse.ProjectDetails projectDetails = new ProjectTaskHierarchyResponse.ProjectDetails(
                project.getId(),
                project.getProjectCode(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                taskHierarchies,
                totalTasks,
                completedTasks,
                project.getCreatedAt(),
                project.getUpdatedAt()
            );
            
            String message = String.format("Retrieved task hierarchy for project %s with %d total tasks", 
                                         projectId, totalTasks);
            logger.info(message);
            
            return new ProjectTaskHierarchyResponse(true, message, projectDetails);
            
        } catch (Exception e) {
            logger.error("Error retrieving task hierarchy for project {}: {}", projectId, e.getMessage());
            throw new RuntimeException("Failed to retrieve task hierarchy for project: " + projectId, e);
        }
    }
    
    private ProjectListResponse.ProjectSummary convertToProjectSummary(Project project) {
        // Get task statistics
        List<Task> projectTasks = taskRepository.findByProjectId(project.getId());
        int totalTasks = projectTasks.size();
        int completedTasks = (int) projectTasks.stream()
            .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
            .count();
        
        return new ProjectListResponse.ProjectSummary(
            project.getId(),
            project.getProjectCode(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getDueDate(),
            project.getNoOfCleaners(),
            project.getAddress(),
            totalTasks,
            completedTasks,
            project.getAverageRating(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
    
    private ProjectTaskHierarchyResponse.TaskHierarchy buildTaskHierarchy(Task task) {
        // Get subtasks
        List<Task> subtasks = taskRepository.findByParentId(task.getId());
        List<ProjectTaskHierarchyResponse.TaskHierarchy> subtaskHierarchies = subtasks.stream()
            .map(this::buildTaskHierarchy)
            .collect(Collectors.toList());
        
        // Since tasks don't have assignment info in the current entity, 
        // we'll set these to null for now
        return new ProjectTaskHierarchyResponse.TaskHierarchy(
            task.getId(),
            task.getName(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            null, // estimatedHours - not in entity
            null, // assignedUserId - not in entity
            null, // assignedUserName - not in entity
            null, // dueDate - removed from tasks, only projects have due dates
            task.getAverageRating(), // averageRating - now included
            task.getCreatedAt(),
            task.getUpdatedAt(),
            subtaskHierarchies
        );
    }

    @Override
    public ProjectTaskReviewsResponse getProjectTaskReviews(String projectId) {
        try {
            // Find the project
            Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
            
            // Get all tasks for this project
            List<Task> allTasks = taskRepository.findByProjectId(projectId);
            
            // Convert to TaskReviewInfo list
            List<ProjectTaskReviewsResponse.TaskReviewInfo> taskReviewInfos = allTasks.stream()
                .map(this::convertToTaskReviewInfo)
                .collect(Collectors.toList());
            
            String message = String.format("Retrieved %d tasks with review information for project %s", 
                                         taskReviewInfos.size(), projectId);
            logger.info(message);
            
            return new ProjectTaskReviewsResponse(true, message, project.getId(), 
                                                project.getName(), taskReviewInfos);
            
        } catch (Exception e) {
            logger.error("Error retrieving task reviews for project {}: {}", projectId, e.getMessage());
            throw new RuntimeException("Failed to retrieve task reviews for project: " + projectId, e);
        }
    }

    private ProjectTaskReviewsResponse.TaskReviewInfo convertToTaskReviewInfo(Task task) {
        // Find review for this task
        Review review = reviewRepository.findByTaskId(task.getId()).orElse(null);
        
        String reviewComment = null;
        Float rating = null;
        boolean hasReview = false;
        
        if (review != null) {
            reviewComment = review.getComment();
            rating = review.getRating().floatValue();
            hasReview = true;
        }
        
        return new ProjectTaskReviewsResponse.TaskReviewInfo(
            task.getId(),
            task.getName(),
            reviewComment,
            rating,
            hasReview
        );
    }
}
