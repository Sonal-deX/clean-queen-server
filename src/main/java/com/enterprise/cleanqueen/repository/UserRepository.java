package com.enterprise.cleanqueen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findByRole(Role role);

    List<User> findByIsActive(Boolean isActive);

    List<User> findByIsVerified(Boolean isVerified);

    List<User> findByRoleAndIsActive(Role role, Boolean isActive);
}
