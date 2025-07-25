package com.enterprise.cleanqueen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.cleanqueen.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    
    Optional<Review> findByTaskId(String taskId);
    
    boolean existsByTaskId(String taskId);
    
    List<Review> findBySupervisorId(String supervisorId);
    
    List<Review> findByRating(Integer rating);
    
    List<Review> findBySupervisorIdOrderByCreatedAtDesc(String supervisorId);
    
    List<Review> findAllByOrderByCreatedAtDesc();
}