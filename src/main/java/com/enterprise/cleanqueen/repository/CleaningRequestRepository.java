package com.enterprise.cleanqueen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.cleanqueen.entity.CleaningRequest;
import com.enterprise.cleanqueen.enums.CleaningRequestStatus;

@Repository
public interface CleaningRequestRepository extends JpaRepository<CleaningRequest, String> {
    
    List<CleaningRequest> findByStatus(CleaningRequestStatus status);
    
    List<CleaningRequest> findByUserId(String userId);
    
    List<CleaningRequest> findByEmail(String email);
    
    List<CleaningRequest> findByStatusOrderByCreatedAtDesc(CleaningRequestStatus status);
    
    List<CleaningRequest> findAllByOrderByCreatedAtDesc();
}