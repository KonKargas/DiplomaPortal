package com.kkargas.diplomaportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kkargas.diplomaportal.enums.ApplicationStatus;
import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisApplication;
import com.kkargas.diplomaportal.repository.ThesisApplicationRepository;
import com.kkargas.diplomaportal.repository.ThesisAssignmentRepository;

@Service
public class ThesisApplicationService {

    private final ThesisApplicationRepository thesisApplicationRepository;
    private final ThesisAssignmentRepository thesisAssignmentRepository;


    public ThesisApplicationService(
            ThesisApplicationRepository thesisApplicationRepository,
            ThesisAssignmentRepository thesisAssignmentRepository) {

        this.thesisApplicationRepository =
                thesisApplicationRepository;

        this.thesisAssignmentRepository =
                thesisAssignmentRepository;
    }
    
    public List<ThesisApplication> findByThesis(
            Thesis thesis) {

        return thesisApplicationRepository.findByThesis(thesis);
    }

    public List<ThesisApplication> findByThesisAndStatus(
            Thesis thesis,
            ApplicationStatus status) {

        return thesisApplicationRepository
                .findByThesisAndStatus(thesis, status);
    }

    public List<ThesisApplication> findByStudent(
            StudentProfile student) {

        return thesisApplicationRepository.findByStudent(student);
    }

    public List<ThesisApplication> findByProfessorAndStatus(
            ProfessorProfile professor,
            ApplicationStatus status) {

        return thesisApplicationRepository
                .findByThesisSupervisorAndStatus(
                        professor,
                        status
                );
    }

    public boolean hasStudentApplied(
            Thesis thesis,
            StudentProfile student) {

        return thesisApplicationRepository
                .existsByThesisAndStudent(thesis, student);
    }

    public ThesisApplication apply(
            Thesis thesis,
            StudentProfile student,
            String notes) {


        /*
         * A student who already owns a thesis
         * cannot apply for another one.
         */
        if (thesisAssignmentRepository
                .existsByStudent(student)) {

            throw new IllegalStateException(
                    "You have already been assigned a thesis."
            );
        }


        if (hasStudentApplied(thesis, student)) {

            throw new IllegalArgumentException(
                    "Student has already applied for this thesis."
            );
        }


        ThesisApplication application =
                new ThesisApplication();

        application.setThesis(thesis);
        application.setStudent(student);
        application.setNotes(notes);

        application.setStatus(
                ApplicationStatus.PENDING
        );

        application.setApplicationDate(
                LocalDateTime.now()
        );


        return thesisApplicationRepository
                .save(application);
    }

    public ThesisApplication findById(Long applicationId) {
        return thesisApplicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Application not found."
                        ));
    }

    public ThesisApplication save(
            ThesisApplication application) {

        return thesisApplicationRepository.save(application);
    }
    
    public ThesisApplication acceptApplication(Long applicationId) {

        ThesisApplication application = findById(applicationId);

        application.setStatus(ApplicationStatus.ACCEPTED);

        return thesisApplicationRepository.save(application);
    }
    
    public ThesisApplication rejectApplication(Long applicationId) {

        ThesisApplication application = findById(applicationId);

        application.setStatus(ApplicationStatus.REJECTED);

        return thesisApplicationRepository.save(application);
    }
    
    public List<ThesisApplication> findByStudentAndStatus(
            StudentProfile student,
            ApplicationStatus status) {

        return thesisApplicationRepository
                .findByStudentAndStatus(
                        student,
                        status
                );
    }
}