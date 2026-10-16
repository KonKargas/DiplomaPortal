package com.kkargas.diplomaportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkargas.diplomaportal.enums.ApplicationStatus;
import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisApplication;


public interface ThesisApplicationRepository
        extends JpaRepository<ThesisApplication, Long> {


    List<ThesisApplication> findByThesis(
            Thesis thesis
    );


    List<ThesisApplication> findByThesisAndStatus(
            Thesis thesis,
            ApplicationStatus status
    );


    List<ThesisApplication> findByStudent(
            StudentProfile student
    );


    Optional<ThesisApplication> findByThesisAndStudent(
            Thesis thesis,
            StudentProfile student
    );


    boolean existsByThesisAndStudent(
            Thesis thesis,
            StudentProfile student
    );


    List<ThesisApplication> findByThesisSupervisorAndStatus(
            ProfessorProfile supervisor,
            ApplicationStatus status
    );


    List<ThesisApplication> findByStudentAndStatus(
            StudentProfile student,
            ApplicationStatus status
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