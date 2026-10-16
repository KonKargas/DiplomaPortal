package com.kkargas.diplomaportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.User;

public interface ProfessorProfileRepository extends JpaRepository<ProfessorProfile, Long> {

	Optional<ProfessorProfile> findByUser(User user);
	
	boolean existsByUser(User user);
	
	void deleteByUser(User user);
}
