package com.enterprise.cleanqueen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.task.UpdateTaskStatusRequest;
import com.enterprise.cleanqueen.dto.task.BulkUpdateTaskStatusResponse;
import com.enterprise.cleanqueen.dto.task.TaskStatusUpdate;
import com.enterprise.cleanqueen.dto.task.TaskUpdateResult;
import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.enums.TaskStatus;
import com.enterprise.cleanqueen.exception.BusinessException;
import com.enterprise.cleanqueen.repository.TaskRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public BulkUpdateTaskStatusResponse updateTasksStatus(UpdateTaskStatusRequest request, String supervisorEmail) {
        logger.info("Bulk updating task statuses by supervisor: {}", supervisorEmail);

        // Verify supervisor exists and has proper role
        userRepository.findByEmail(supervisorEmail)
                .orElseThrow(() -> new BusinessException("Supervisor not found"));

        List<TaskUpdateResult> results = new ArrayList<>();
        int successCount = 0;
        
        for (TaskStatusUpdate taskUpdate : request.getTasks()) {
            try {
                // Find the task
                Task task = taskRepository.findById(taskUpdate.getTaskId()).orElse(null);
                
                if (task == null) {
                    results.add(new TaskUpdateResult(
                        taskUpdate.getTaskId(),
                        "Unknown",
                        false,
                        "Task not found with ID: " + taskUpdate.getTaskId(),
                        null,
                        null
                    ));
                    continue;
                }
                
                TaskStatus previousStatus = task.getStatus();
                
                // Update task status
                task.setStatus(taskUpdate.getStatus());
                
                // Save updated task
                taskRepository.save(task);
                
                results.add(new TaskUpdateResult(
                    task.getId(),
                    task.getName(),
                    true,
                    "Task status updated successfully" + (taskUpdate.getRemarks() != null ? " - " + taskUpdate.getRemarks() : ""),
                    previousStatus,
                    task.getStatus()
                ));
                
                successCount++;
                
                logger.info("Task {} status updated from {} to {} by supervisor {}", 
                           task.getId(), previousStatus, taskUpdate.getStatus(), supervisorEmail);
                
            } catch (Exception e) {
                logger.error("Error updating task {}: {}", taskUpdate.getTaskId(), e.getMessage());
                results.add(new TaskUpdateResult(
                    taskUpdate.getTaskId(),
                    "Unknown",
                    false,
                    "Error updating task: " + e.getMessage(),
                    null,
                    null
                ));
            }
        }
        
        boolean overallSuccess = successCount > 0;
        String message = String.format("Updated %d out of %d tasks successfully", 
                                     successCount, request.getTasks().size());
        
        logger.info("Bulk update completed: {}", message);
        
        return new BulkUpdateTaskStatusResponse(overallSuccess, message, results);
    }
}