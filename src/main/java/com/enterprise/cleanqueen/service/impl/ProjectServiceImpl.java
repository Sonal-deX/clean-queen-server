package com.enterprise.cleanqueen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;
import com.enterprise.cleanqueen.dto.project.TaskCreateRequest;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.enums.ProjectStatus;
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
