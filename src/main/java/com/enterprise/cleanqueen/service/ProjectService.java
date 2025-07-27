package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;

public interface ProjectService {

    ProjectCreateResponse createProject(ProjectCreateRequest request);
}
