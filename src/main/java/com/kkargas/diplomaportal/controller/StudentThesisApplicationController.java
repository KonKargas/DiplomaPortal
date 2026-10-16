package com.kkargas.diplomaportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.StudentProfileService;
import com.kkargas.diplomaportal.service.ThesisApplicationService;
import com.kkargas.diplomaportal.service.ThesisService;
import com.kkargas.diplomaportal.service.UserService;

@Controller
public class StudentThesisApplicationController {

    private final UserService userService;
    private final StudentProfileService studentProfileService;
    private final ThesisService thesisService;
    private final ThesisApplicationService thesisApplicationService;

    public StudentThesisApplicationController(
            UserService userService,
            StudentProfileService studentProfileService,
            ThesisService thesisService,
            ThesisApplicationService thesisApplicationService) {

        this.userService = userService;
        this.studentProfileService = studentProfileService;
        this.thesisService = thesisService;
        this.thesisApplicationService = thesisApplicationService;
    }


    @GetMapping("/student/theses/{id}/apply")
    public String getApplicationPage(
            @PathVariable("id") Long thesisId,
            Authentication authentication,
            Model model) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        StudentProfile student =
                studentProfileService.findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Student profile not found."
                                ));

        Thesis thesis =
                thesisService.findById(thesisId);

        boolean alreadyApplied =
                thesisApplicationService
                        .hasStudentApplied(thesis, student);

        model.addAttribute("student", student);
        model.addAttribute("thesis", thesis);
        model.addAttribute("alreadyApplied", alreadyApplied);

        return "student/ThesisApplicationPage";
    }


    @PostMapping("/student/theses/{id}/apply")
    public String applyForThesis(
            @PathVariable("id") Long thesisId,
            @RequestParam(value = "notes", required = false)
            String notes,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        StudentProfile student =
                studentProfileService.findByUser(user)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Student profile not found."
                                ));

        Thesis thesis =
                thesisService.findById(thesisId);

        thesisApplicationService.apply(
                thesis,
                student,
                notes
        );

        return "redirect:/student/dashboard";
    }
}