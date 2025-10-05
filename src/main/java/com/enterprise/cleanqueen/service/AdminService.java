package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;
import com.enterprise.cleanqueen.dto.admin.DeleteTaskResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllCleaningRequestsResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllCustomersResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllSupervisorsResponse;
import com.enterprise.cleanqueen.dto.admin.GetAllProjectsResponse;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeRequest;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeResponse;

public interface AdminService {
    
    CreateSupervisorResponse createSupervisor(CreateSupervisorRequest request);

    SendProjectCodeResponse sendProjectCode(SendProjectCodeRequest request);

    GetAllSupervisorsResponse getAllSupervisors();

    GetAllCustomersResponse getAllCustomers();

    GetAllCleaningRequestsResponse getAllCleaningRequests();

    DeleteTaskResponse deleteTask(String taskId);

    GetAllProjectsResponse getAllProjects();

}