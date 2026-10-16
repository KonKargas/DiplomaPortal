package com.kkargas.diplomaportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkargas.diplomaportal.enums.UserRole;
import com.kkargas.diplomaportal.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    
    List<User> findByUserRole(UserRole userRole);
}