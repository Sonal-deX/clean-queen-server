package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.project.ProjectCreateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectCreateResponse;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateRequest;
import com.enterprise.cleanqueen.dto.project.ProjectUpdateResponse;

public interface ProjectService {

    ProjectCreateResponse createProject(ProjectCreateRequest request);

    ProjectUpdateResponse updateProject(String projectId, ProjectUpdateRequest request);
}
