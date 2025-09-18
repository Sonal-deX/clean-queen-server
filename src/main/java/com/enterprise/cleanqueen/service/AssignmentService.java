package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.assignment.AssignCustomerRequest;
import com.enterprise.cleanqueen.dto.assignment.AssignSupervisorRequest;
import com.enterprise.cleanqueen.dto.assignment.AssignmentResponse;
import com.enterprise.cleanqueen.dto.assignment.SupervisorExitRequest;
import com.enterprise.cleanqueen.dto.assignment.SupervisorExitResponse;

public interface AssignmentService {

    AssignmentResponse assignCustomerToProject(AssignCustomerRequest request, String userEmail);

    AssignmentResponse assignSupervisorToProject(AssignSupervisorRequest request, String userEmail);

    SupervisorExitResponse supervisorExitProject(SupervisorExitRequest request, String userEmail);
}
