package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.task.UpdateTaskStatusRequest;
import com.enterprise.cleanqueen.dto.task.BulkUpdateTaskStatusResponse;

public interface TaskService {
    
    BulkUpdateTaskStatusResponse updateTasksStatus(UpdateTaskStatusRequest request, String supervisorId);
    
}