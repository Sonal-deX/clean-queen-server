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
import com.enterprise.cleanqueen.dto.project.ProjectUpdateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateResponse;
import com.enterprise.cleanqueen.dto.project.TaskCreateRequest;
import com.enterprise.cleanqueen.dto.project.TaskUpdateRequest;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.enums.ProjectStatus;
import com.enterprise.cleanqueen.enums.TaskPriority;
import com.enterprise.cleanqueen.enums.TaskStatus;
import com.enterprise.cleanqueen.repository.ProjectRepository;
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
            task.setStatus(taskRequest.getStatus() != null ? taskRequest.getStatus() : TaskStatus.PENDING);
            task.setPriority(taskRequest.getPriority() != null ? taskRequest.getPriority() : TaskPriority.MEDIUM);
            task.setDueDate(taskRequest.getDueDate());
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
            task.setStatus(TaskStatus.PENDING);
            task.setPriority(taskRequest.getPriority() != null ? taskRequest.getPriority() : com.enterprise.cleanqueen.enums.TaskPriority.MEDIUM);
            task.setDueDate(taskRequest.getDueDate());
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
}
