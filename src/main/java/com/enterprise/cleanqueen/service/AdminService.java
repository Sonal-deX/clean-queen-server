package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.admin.CreateSupervisorRequest;
import com.enterprise.cleanqueen.dto.admin.CreateSupervisorResponse;

public interface AdminService {
    
    CreateSupervisorResponse createSupervisor(CreateSupervisorRequest request);
}