package com.enterprise.cleanqueen.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.entity.OtpVerification;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    
    Optional<OtpVerification> findByEmailAndOtpAndIsUsedFalse(String email, String otp);
    
    Optional<OtpVerification> findTopByEmailAndIsUsedFalseOrderByCreatedAtDesc(String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE OtpVerification o SET o.isUsed = true WHERE o.email = :email AND o.isUsed = false")
    void markAllAsUsedByEmail(@Param("email") String email);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpVerification o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);
    
    void deleteByEmail(String email);
}