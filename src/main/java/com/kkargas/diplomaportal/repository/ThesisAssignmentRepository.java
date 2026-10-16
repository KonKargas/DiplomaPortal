package com.kkargas.diplomaportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisAssignment;


public interface ThesisAssignmentRepository
        extends JpaRepository<ThesisAssignment, Long> {


    Optional<ThesisAssignment> findByStudent(
            StudentProfile student
    );


    Optional<ThesisAssignment> findByThesis(
            Thesis thesis
    );


    boolean existsByStudent(
            StudentProfile student
    );


    boolean existsByThesis(
            Thesis thesis
    );


    List<ThesisAssignment> findByThesisSupervisor(
            ProfessorProfile professor
    );


    /*
     * Used when a thesis is deleted.
     */
    void deleteByThesis(
            Thesis thesis
    );


    /*
     * Used when a student account is deleted.
     */
    void deleteByStudent(
            StudentProfile student
    );
}