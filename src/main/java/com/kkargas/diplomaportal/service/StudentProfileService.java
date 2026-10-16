package com.kkargas.diplomaportal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.repository.StudentProfileRepository;
import com.kkargas.diplomaportal.repository.ThesisApplicationRepository;
import com.kkargas.diplomaportal.repository.ThesisAssignmentRepository;


@Service
public class StudentProfileService {


    private final StudentProfileRepository studentProfileRepository;

    private final ThesisApplicationRepository thesisApplicationRepository;

    private final ThesisAssignmentRepository thesisAssignmentRepository;


    public StudentProfileService(
            StudentProfileRepository studentProfileRepository,
            ThesisApplicationRepository thesisApplicationRepository,
            ThesisAssignmentRepository thesisAssignmentRepository) {

        this.studentProfileRepository =
                studentProfileRepository;

        this.thesisApplicationRepository =
                thesisApplicationRepository;

        this.thesisAssignmentRepository =
                thesisAssignmentRepository;
    }


    public Optional<StudentProfile> findByUser(
            User user) {

        return studentProfileRepository
                .findByUser(user);
    }


    public boolean profileExists(
            User user) {

        return studentProfileRepository
                .existsByUser(user);
    }


    public StudentProfile save(
            StudentProfile studentProfile) {

        return studentProfileRepository
                .save(studentProfile);
    }


    /*
     * Delete the complete student-related domain data.
     *
     * Order:
     *
     * ThesisAssignment
     *      ↓
     * ThesisApplications
     *      ↓
     * StudentProfile
     *
     * The User entity itself is deleted afterwards
     * by AdminUserService.
     */
    @Transactional
    public void deleteByUser(
            User user) {

        StudentProfile student =
                studentProfileRepository
                        .findByUser(user)
                        .orElse(null);


        /*
         * A user may theoretically exist without
         * having completed their profile yet.
         */
        if (student == null) {
            return;
        }


        /*
         * Delete the student's assigned thesis relation.
         *
         * The actual thesis topic is NOT deleted.
         */
        thesisAssignmentRepository
                .deleteByStudent(
                        student
                );


        /*
         * Delete every application ever submitted
         * by this student.
         */
        thesisApplicationRepository
                .deleteByStudent(
                        student
                );


        /*
         * Finally delete the student profile.
         */
        studentProfileRepository
                .delete(
                        student
                );
    }
}