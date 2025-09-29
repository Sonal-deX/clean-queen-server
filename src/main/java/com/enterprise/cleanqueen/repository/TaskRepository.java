package com.enterprise.cleanqueen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.enums.TaskPriority;
import com.enterprise.cleanqueen.enums.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    
    List<Task> findByProjectId(String projectId);
    
    List<Task> findByParentId(String parentId);
    
    List<Task> findByStatus(TaskStatus status);
    
    List<Task> findByPriority(TaskPriority priority);
    
    List<Task> findByProjectIdAndParentId(String projectId, String parentId);
    
    List<Task> findByProjectIdAndParentIdIsNull(String projectId); // Root tasks
    
    List<Task> findByProjectIdAndStatus(String projectId, TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND t.parentId IS NULL")
    List<Task> findRootTasksByProjectId(@Param("projectId") String projectId);
    
    @Query("SELECT t FROM Task t WHERE t.parentId = :parentId")
    List<Task> findChildrenTasks(@Param("parentId") String parentId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.parentId = :parentId")
    long countChildrenTasks(@Param("parentId") String parentId);
    
    List<Task> findByIsActive(Boolean isActive);
    
    List<Task> findByProjectIdAndIsActive(String projectId, Boolean isActive);
}