package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeRequest;
import com.enterprise.cleanqueen.dto.admin.SendProjectCodeResponse;

public interface AdminService {
    
    CreateSupervisorResponse createSupervisor(CreateSupervisorRequest request);

    SendProjectCodeResponse sendProjectCode(SendProjectCodeRequest request);

}