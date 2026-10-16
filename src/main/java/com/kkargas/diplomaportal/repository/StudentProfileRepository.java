package com.kkargas.diplomaportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.User;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUser(User user);

    boolean existsByUser(User user);
    
    void deleteByUser(User user);
}