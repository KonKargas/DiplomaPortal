package com.kkargas.diplomaportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.repository.ProfessorProfileRepository;


@Service
public class ProfessorProfileService {


    private final ProfessorProfileRepository professorProfileRepository;

    private final ThesisService thesisService;


    public ProfessorProfileService(
            ProfessorProfileRepository professorProfileRepository,
            ThesisService thesisService) {

        this.professorProfileRepository =
                professorProfileRepository;

        this.thesisService =
                thesisService;
    }


    public Optional<ProfessorProfile> findByUser(
            User user) {

        return professorProfileRepository
                .findByUser(user);
    }


    public boolean profileExists(
            User user) {

        return professorProfileRepository
                .existsByUser(user);
    }


    public ProfessorProfile save(
            ProfessorProfile professorProfile) {

        return professorProfileRepository
                .save(professorProfile);
    }


    /*
     * Delete the complete professor-related domain data.
     *
     * For each thesis:
     *
     * ThesisAssignment
     *      ↓
     * ThesisApplications
     *      ↓
     * Thesis
     *
     * Then:
     *
     * ProfessorProfile
     *
     * The User entity itself is deleted afterwards
     * by AdminUserService.
     */
    @Transactional
    public void deleteByUser(
            User user) {

        ProfessorProfile professor =
                professorProfileRepository
                        .findByUser(user)
                        .orElse(null);


        /*
         * A professor user may theoretically exist
         * without having completed a profile.
         */
        if (professor == null) {
            return;
        }


        /*
         * Get every thesis created by
         * this professor.
         */
        List<Thesis> theses =
                thesisService
                        .findBySupervisor(
                                professor
                        );


        /*
         * deleteThesis() already removes:
         *
         * 1. assignment
         * 2. applications
         * 3. thesis
         */
        for (Thesis thesis : theses) {

            thesisService.deleteThesis(
                    thesis.getThesisId()
            );
        }


        /*
         * Finally delete professor profile.
         *
         * Student accounts are NOT deleted.
         */
        professorProfileRepository
                .delete(
                        professor
                );
    }
}