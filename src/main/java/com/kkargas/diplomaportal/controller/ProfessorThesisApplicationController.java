package com.kkargas.diplomaportal.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kkargas.diplomaportal.dto.ThesisAssignmentRequest;
import com.kkargas.diplomaportal.enums.ApplicationStatus;
import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisApplication;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.ProfessorProfileService;
import com.kkargas.diplomaportal.service.ThesisApplicationService;
import com.kkargas.diplomaportal.service.ThesisAssignmentService;
import com.kkargas.diplomaportal.service.ThesisService;
import com.kkargas.diplomaportal.service.UserService;

@Controller
public class ProfessorThesisApplicationController {

    private final UserService userService;
    private final ProfessorProfileService professorProfileService;
    private final ThesisService thesisService;
    private final ThesisApplicationService thesisApplicationService;
    private final ThesisAssignmentService thesisAssignmentService;

    public ProfessorThesisApplicationController(
            UserService userService,
            ProfessorProfileService professorProfileService,
            ThesisService thesisService,
            ThesisApplicationService thesisApplicationService,
            ThesisAssignmentService thesisAssignmentService) {

        this.userService = userService;
        this.professorProfileService =
                professorProfileService;

        this.thesisService = thesisService;

        this.thesisApplicationService =
                thesisApplicationService;

        this.thesisAssignmentService =
                thesisAssignmentService;
    }
    
    @PostMapping("/professor/theses/{thesisId}/assign")
    public String assignThesis(
            @PathVariable Long thesisId,
            @ModelAttribute("assignmentRequest")
            ThesisAssignmentRequest assignmentRequest,
            Authentication authentication) {

        ProfessorProfile professor =
                getAuthenticatedProfessor(
                        authentication
                );


        Thesis thesis =
                thesisService.findById(
                        thesisId
                );


        if (!thesis.getSupervisor()
                .getProfessorProfileId()
                .equals(
                        professor.getProfessorProfileId()
                )) {

            throw new IllegalStateException(
                    "You are not allowed to assign this thesis."
            );
        }


        thesisAssignmentService.assignStudent(
                thesis,
                assignmentRequest
        );


        return "redirect:/professor/theses/"
                + thesisId
                + "/applications";
    }


    @GetMapping("/professor/theses/{thesisId}/applications")
    public String viewApplications(
            @PathVariable Long thesisId,
            Authentication authentication,
            Model model) {

        ProfessorProfile professor =
                getAuthenticatedProfessor(authentication);

        Thesis thesis =
                thesisService.findById(thesisId);


        // Security / ownership check
        if (!thesis.getSupervisor()
                .getProfessorProfileId()
                .equals(professor.getProfessorProfileId())) {

            throw new IllegalStateException(
                    "You are not allowed to view applications for this thesis."
            );
        }


        List<ThesisApplication> applications =
                thesisApplicationService.findByThesis(thesis);


        boolean hasPendingApplications =
                applications.stream()
                        .anyMatch(thesisApplication ->
                                thesisApplication.getStatus()
                                        == ApplicationStatus.PENDING
                        );


        model.addAttribute(
                "professor",
                professor
        );

        model.addAttribute(
                "thesis",
                thesis
        );

        model.addAttribute(
                "applications",
                applications
        );

        model.addAttribute(
                "assignmentRequest",
                new ThesisAssignmentRequest()
        );

        model.addAttribute(
                "hasPendingApplications",
                hasPendingApplications
        );


        return "professor/ThesisApplicationsPage";
    }

    private ProfessorProfile getAuthenticatedProfessor(
            Authentication authentication) {

        String username =
                authentication.getName();

        User user =
                userService.findByUsername(username);

        return professorProfileService
                .findByUser(user)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Professor profile not found."
                        ));
    }
}