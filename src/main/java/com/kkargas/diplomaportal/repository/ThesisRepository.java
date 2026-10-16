package com.kkargas.diplomaportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.Thesis;

public interface ThesisRepository extends JpaRepository<Thesis, Long> {

    List<Thesis> findBySupervisor(ProfessorProfile supervisor);
}