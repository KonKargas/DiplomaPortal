package com.kkargas.diplomaportal.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kkargas.diplomaportal.dto.ThesisGradeForm;
import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisAssignment;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.ProfessorProfileService;
import com.kkargas.diplomaportal.service.ThesisAssignmentService;
import com.kkargas.diplomaportal.service.ThesisService;
import com.kkargas.diplomaportal.service.UserService;

@Controller
public class ProfessorDashboardController {

    private final UserService userService;
    private final ProfessorProfileService professorProfileService;
    private final ThesisService thesisService;
    private final ThesisAssignmentService thesisAssignmentService;
    
    public ProfessorDashboardController(
            UserService userService,
            ProfessorProfileService professorProfileService,
            ThesisService thesisService,
            ThesisAssignmentService thesisAssignmentService) {

        this.userService = userService;
        this.professorProfileService = professorProfileService;
        this.thesisService = thesisService;
        this.thesisAssignmentService = thesisAssignmentService;
    }

    @GetMapping("/professor/dashboard")
    public String getProfessorDashboard(
            Authentication authentication,
            Model model) {

        String username =
                authentication.getName();

        User user =
                userService.findByUsername(username);

        ProfessorProfile professor =
                professorProfileService
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Professor profile not found."
                                )
                        );


        List<Thesis> theses =
                thesisService.findBySupervisor(
                        professor
                );


        List<ThesisAssignment> assignments =
                thesisAssignmentService
                        .findByProfessor(
                                professor
                        );


        model.addAttribute(
                "professor",
                professor
        );

        model.addAttribute(
                "theses",
                theses
        );

        model.addAttribute(
                "assignments",
                assignments
        );


        return "dashboard/ProfessorDashboard";
    }
    
    @GetMapping("/professor/theses/create")
    public String getThesisFormPage(Model model) {

        model.addAttribute(
                "thesis",
                new Thesis()
        );

        model.addAttribute(
                "editMode",
                false
        );

        return "professor/ThesisFormPage";
    }
    
    @PostMapping("/professor/theses/create")
    public String saveThesis(
            @ModelAttribute("thesis") Thesis thesis,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        ProfessorProfile professor = professorProfileService
                .findByUser(user)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Professor profile not found."
                        ));

        thesis.setSupervisor(professor);

        thesisService.save(thesis);

        return "redirect:/professor/dashboard";
    }
    
    @GetMapping("/professor/theses/{thesisId}/edit")
    public String getEditThesisPage(
            @PathVariable Long thesisId,
            Authentication authentication,
            Model model) {

        String username =
                authentication.getName();


        User user =
                userService.findByUsername(username);


        ProfessorProfile professor =
                professorProfileService
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Professor profile not found."
                                )
                        );


        Thesis thesis =
                thesisService.findById(
                        thesisId
                );


        /*
         * Ownership check
         */
        if (!thesis.getSupervisor()
                .getProfessorProfileId()
                .equals(
                        professor.getProfessorProfileId()
                )) {

            throw new IllegalStateException(
                    "You are not allowed to edit this thesis."
            );
        }


        model.addAttribute(
                "thesis",
                thesis
        );
        
        model.addAttribute(
                "editMode",
                true
        );

        return "professor/ThesisFormPage";
    }
    
    @PostMapping("/professor/theses/{thesisId}/edit")
    public String updateThesis(
            @PathVariable Long thesisId,
            @ModelAttribute("thesis")
            Thesis formThesis,
            Authentication authentication) {

        String username =
                authentication.getName();


        User user =
                userService.findByUsername(username);


        ProfessorProfile professor =
                professorProfileService
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Professor profile not found."
                                )
                        );


        Thesis thesis =
                thesisService.findById(
                        thesisId
                );


        /*
         * Ownership check
         */
        if (!thesis.getSupervisor()
                .getProfessorProfileId()
                .equals(
                        professor.getProfessorProfileId()
                )) {

            throw new IllegalStateException(
                    "You are not allowed to edit this thesis."
            );
        }


        thesisService.updateThesis(
                thesisId,
                formThesis
        );


        return "redirect:/professor/dashboard";
    }
    
    @PostMapping("/professor/theses/{thesisId}/delete")
    public String deleteThesis(
            @PathVariable Long thesisId,
            Authentication authentication) {

        String username =
                authentication.getName();


        User user =
                userService.findByUsername(username);


        ProfessorProfile professor =
                professorProfileService
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Professor profile not found."
                                )
                        );


        Thesis thesis =
                thesisService.findById(
                        thesisId
                );


        /*
         * Ownership check
         */
        if (!thesis.getSupervisor()
                .getProfessorProfileId()
                .equals(
                        professor.getProfessorProfileId()
                )) {

            throw new IllegalStateException(
                    "You are not allowed to delete this thesis."
            );
        }


        thesisService.deleteThesis(
                thesisId
        );


        return "redirect:/professor/dashboard";
    }
    
    @GetMapping("/professor/assignments/{assignmentId}/grade")
    public String getGradePage(
            @PathVariable Long assignmentId,
            Authentication authentication,
            Model model) {

        String username =
                authentication.getName();


        User user =
                userService.findByUsername(username);


        ProfessorProfile professor =
                professorProfileService
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Professor profile not found."
                                )
                        );


        ThesisAssignment assignment =
                thesisAssignmentService
                        .findById(assignmentId);


        /*
         * Security / ownership check:
         * only the supervisor of this thesis
         * can grade the assignment.
         */
        if (!assignment.getThesis()
                .getSupervisor()
                .getProfessorProfileId()
                .equals(
                        professor.getProfessorProfileId()
                )) {

            throw new IllegalStateException(
                    "You are not allowed to grade this thesis."
            );
        }


        ThesisGradeForm gradeForm =
                new ThesisGradeForm();


        /*
         * If grades already exist,
         * load them into the form.
         */
        gradeForm.setImplementationGrade(
                assignment.getImplementationGrade()
        );

        gradeForm.setReportGrade(
                assignment.getReportGrade()
        );

        gradeForm.setPresentationGrade(
                assignment.getPresentationGrade()
        );


        model.addAttribute(
                "assignment",
                assignment
        );

        model.addAttribute(
                "gradeForm",
                gradeForm
        );


        return "professor/ThesisGradePage";
    }
    
    @PostMapping("/professor/assignments/{assignmentId}/grade")
    public String saveGrades(
            @PathVariable Long assignmentId,
            @ModelAttribute("gradeForm")
            ThesisGradeForm gradeForm,
            Authentication authentication) {

        String username =
                authentication.getName();


        User user =
                userService.findByUsername(username);


        ProfessorProfile professor =
                professorProfileService
                        .findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Professor profile not found."
                                )
                        );


        ThesisAssignment assignment =
                thesisAssignmentService
                        .findById(assignmentId);


        /*
         * Security / ownership check.
         */
        if (!assignment.getThesis()
                .getSupervisor()
                .getProfessorProfileId()
                .equals(
                        professor.getProfessorProfileId()
                )) {

            throw new IllegalStateException(
                    "You are not allowed to grade this thesis."
            );
        }


        thesisAssignmentService.gradeAssignment(
                assignmentId,
                gradeForm
        );


        return "redirect:/professor/dashboard";
    }
    
}