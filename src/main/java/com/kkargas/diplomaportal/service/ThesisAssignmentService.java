package com.kkargas.diplomaportal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kkargas.diplomaportal.dto.ThesisAssignmentRequest;
import com.kkargas.diplomaportal.dto.ThesisGradeForm;
import com.kkargas.diplomaportal.enums.ApplicationStatus;
import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisApplication;
import com.kkargas.diplomaportal.model.ThesisAssignment;
import com.kkargas.diplomaportal.repository.ThesisAssignmentRepository;
import com.kkargas.diplomaportal.strategy.ThesisAssignmentStrategy;
import com.kkargas.diplomaportal.strategy.ThesisAssignmentStrategyFactory;

@Service
public class ThesisAssignmentService {

    private final ThesisApplicationService thesisApplicationService;

    private final ThesisAssignmentRepository thesisAssignmentRepository;

    private final ThesisAssignmentStrategyFactory strategyFactory;


    public ThesisAssignmentService(
            ThesisApplicationService thesisApplicationService,
            ThesisAssignmentRepository thesisAssignmentRepository,
            ThesisAssignmentStrategyFactory strategyFactory) {

        this.thesisApplicationService =
                thesisApplicationService;

        this.thesisAssignmentRepository =
                thesisAssignmentRepository;

        this.strategyFactory =
                strategyFactory;
    }
    
    public Optional<ThesisAssignment> getThesisAssignment(StudentProfile studentProfile) {
    		return this.thesisAssignmentRepository.findByStudent(studentProfile);
    }
    
    public ThesisAssignment findById(Long assignmentId) {

        return thesisAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Thesis assignment not found."
                        )
                );
    }
    
    public List<ThesisAssignment> findByProfessor(
            ProfessorProfile professor) {

        return thesisAssignmentRepository
                .findByThesisSupervisor(professor);
    }
    
    @Transactional
    public ThesisAssignment gradeAssignment(
            Long assignmentId,
            ThesisGradeForm form) {

        ThesisAssignment assignment =
                findById(assignmentId);


        assignment.setImplementationGrade(
                form.getImplementationGrade()
        );

        assignment.setReportGrade(
                form.getReportGrade()
        );

        assignment.setPresentationGrade(
                form.getPresentationGrade()
        );


        double finalGrade =
                (
                    form.getImplementationGrade()
                    + form.getReportGrade()
                    + form.getPresentationGrade()
                ) / 3.0;

        finalGrade =
                Math.round(finalGrade * 100.0) / 100.0;
        
        assignment.setFinalGrade(finalGrade);

        return thesisAssignmentRepository.save(
                assignment
        );
    }

    @Transactional
    public ThesisAssignment assignStudent(
            Thesis thesis,
            ThesisAssignmentRequest request) {


        /*
         * The thesis itself must not already
         * have been assigned.
         */
        if (thesisAssignmentRepository
                .existsByThesis(thesis)) {

            throw new IllegalStateException(
                    "This thesis has already been assigned."
            );
        }


        /*
         * Get all pending applications
         * for this thesis.
         */
        List<ThesisApplication> applications =
                thesisApplicationService
                        .findByThesisAndStatus(
                                thesis,
                                ApplicationStatus.PENDING
                        );


        if (applications.isEmpty()) {

            throw new IllegalStateException(
                    "There are no pending applications for this thesis."
            );
        }


        /*
         * Remove students who have already
         * been assigned another thesis.
         */
        List<ThesisApplication> eligibleApplications =
                applications.stream()
                        .filter(application ->
                                !thesisAssignmentRepository
                                        .existsByStudent(
                                                application.getStudent()
                                        )
                        )
                        .toList();


        if (eligibleApplications.isEmpty()) {

            throw new IllegalStateException(
                    "There are no eligible students for this thesis."
            );
        }


        /*
         * Select the requested strategy.
         */
        ThesisAssignmentStrategy strategy =
                strategyFactory.getStrategy(
                        request.getStrategyType()
                );


        /*
         * Strategy selects one eligible application.
         */
        ThesisApplication selectedApplication =
                strategy.selectStudent(
                        eligibleApplications,
                        request.getGradeThreshold(),
                        request.getCoursesThreshold()
                );


        /*
         * Create the real thesis assignment.
         */
        ThesisAssignment assignment =
                new ThesisAssignment();

        assignment.setThesis(thesis);

        assignment.setStudent(
                selectedApplication.getStudent()
        );

        assignment.setAssignedAt(
                LocalDateTime.now()
        );


        thesisAssignmentRepository.save(
                assignment
        );


        /*
         * Selected application -> ACCEPTED
         *
         * All other applications for this thesis
         * -> REJECTED
         */
        for (ThesisApplication application
                : applications) {

            if (application
                    .getApplicationId()
                    .equals(
                            selectedApplication
                                    .getApplicationId()
                    )) {

                application.setStatus(
                        ApplicationStatus.ACCEPTED
                );

            } else {

                application.setStatus(
                        ApplicationStatus.REJECTED
                );
            }


            thesisApplicationService.save(
                    application
            );
        }


        /*
         * The selected student may have pending
         * applications for other thesis topics.
         *
         * Those applications must now be rejected.
         */
        List<ThesisApplication> otherApplications =
                thesisApplicationService
                        .findByStudentAndStatus(
                                selectedApplication.getStudent(),
                                ApplicationStatus.PENDING
                        );


        for (ThesisApplication application
                : otherApplications) {

            application.setStatus(
                    ApplicationStatus.REJECTED
            );

            thesisApplicationService.save(
                    application
            );
        }


        return assignment;
    }
    
    @Transactional
    public ThesisAssignment uploadThesisPdf(
            StudentProfile student,
            MultipartFile pdfFile) {

        if (pdfFile == null || pdfFile.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a PDF file."
            );
        }


        String originalFilename =
                pdfFile.getOriginalFilename();


        if (originalFilename == null
                || !originalFilename.toLowerCase().endsWith(".pdf")) {

            throw new IllegalArgumentException(
                    "Only PDF files are allowed."
            );
        }


        String contentType =
                pdfFile.getContentType();


        if (contentType != null
                && !contentType.equalsIgnoreCase("application/pdf")) {

            throw new IllegalArgumentException(
                    "Invalid file type. Only PDF files are allowed."
            );
        }


        ThesisAssignment assignment =
                thesisAssignmentRepository
                        .findByStudent(student)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "No thesis assignment found for this student."
                                )
                        );


        try {

            Path uploadDirectory =
                    Paths.get(
                            "uploads",
                            "thesis-pdfs"
                    );


            Files.createDirectories(
                    uploadDirectory
            );


            String storedFilename =
                    UUID.randomUUID()
                    + ".pdf";


            Path targetPath =
                    uploadDirectory.resolve(
                            storedFilename
                    );


            Files.copy(
                    pdfFile.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            /*
             * Remove old PDF if one already exists.
             */
            if (assignment.getPdfFilePath() != null
                    && !assignment.getPdfFilePath().isBlank()) {

                deleteOldPdf(
                        assignment.getPdfFilePath()
                );
            }


            assignment.setPdfFilePath(
                    "/uploads/thesis-pdfs/"
                    + storedFilename
            );


            assignment.setSubmittedAt(
                    LocalDateTime.now()
            );


            return thesisAssignmentRepository.save(
                    assignment
            );


        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Could not store thesis PDF.",
                    exception
            );
        }
    }
    
    private void deleteOldPdf(
            String pdfFilePath) {

        try {

            String relativePath =
                    pdfFilePath.startsWith("/")
                    ? pdfFilePath.substring(1)
                    : pdfFilePath;


            Path oldFile =
                    Paths.get(relativePath);


            Files.deleteIfExists(
                    oldFile
            );


        } catch (IOException exception) {

            /*
             * We do not interrupt the new upload
             * if deletion of the old file fails.
             */
        }
    }
}