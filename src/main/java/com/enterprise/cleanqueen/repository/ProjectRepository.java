package com.enterprise.cleanqueen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.enums.ProjectStatus;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    
    Optional<Project> findByProjectCode(String projectCode);
    
    boolean existsByProjectCode(String projectCode);
    
    List<Project> findByStatus(ProjectStatus status);
    
    List<Project> findByCustomerId(String customerId);
    
    List<Project> findBySupervisorId(String supervisorId);
    
    List<Project> findByCustomerIdAndStatus(String customerId, ProjectStatus status);
    
    List<Project> findBySupervisorIdAndStatus(String supervisorId, ProjectStatus status);
    
    List<Project> findAllByOrderByCreatedAtDesc();
}