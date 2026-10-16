package com.kkargas.diplomaportal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.model.ThesisApplication;
import com.kkargas.diplomaportal.model.ThesisAssignment;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.StudentProfileService;
import com.kkargas.diplomaportal.service.ThesisApplicationService;
import com.kkargas.diplomaportal.service.ThesisAssignmentService;
import com.kkargas.diplomaportal.service.ThesisService;
import com.kkargas.diplomaportal.service.UserService;

@Controller
public class StudentDashboardController {

    private final UserService userService;
    private final StudentProfileService studentProfileService;
    private final ThesisService thesisService;
    private final ThesisAssignmentService thesisAssignmentService;
    private final ThesisApplicationService thesisApplicationService;


    public StudentDashboardController(
            UserService userService,
            StudentProfileService studentProfileService,
            ThesisService thesisService,
            ThesisApplicationService thesisApplicationService,
            ThesisAssignmentService thesisAssignmentService) {

        this.userService = userService;
        this.studentProfileService = studentProfileService;
        this.thesisService = thesisService;
        this.thesisApplicationService = thesisApplicationService;
        this.thesisAssignmentService = thesisAssignmentService;
    }


    @GetMapping("/student/dashboard")
    public String getStudentDashboard(
            Authentication authentication,
            Model model) {

        StudentProfile student =
                getAuthenticatedStudent(authentication);


        List<Thesis> theses =
                thesisService.findAll();


        List<ThesisApplication> applications =
                thesisApplicationService
                        .findByStudent(student);


        Optional<ThesisAssignment> assignment =
                thesisAssignmentService
                        .getThesisAssignment(student);


        model.addAttribute(
                "student",
                student
        );

        model.addAttribute(
                "theses",
                theses
        );

        model.addAttribute(
                "applications",
                applications
        );

        model.addAttribute(
                "hasThesisAssignment",
                assignment.isPresent()
        );


        return "dashboard/StudentDashboard";
    }


    @GetMapping("/student/assignment")
    public String getThesisAssignment(
            Authentication authentication,
            Model model) {

        StudentProfile student =
                getAuthenticatedStudent(authentication);


        ThesisAssignment assignment =
                thesisAssignmentService
                        .getThesisAssignment(student)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "No thesis has been assigned to this student."
                                )
                        );


        model.addAttribute(
                "student",
                student
        );

        model.addAttribute(
                "assignment",
                assignment
        );


        return "student/ThesisAssignment";
    }


    private StudentProfile getAuthenticatedStudent(
            Authentication authentication) {

        String username =
                authentication.getName();


        User user =
                userService.findByUsername(username);


        return studentProfileService
                .findByUser(user)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student profile not found."
                        )
                );
    }
    
    @PostMapping("/student/assignment/upload")
    public String uploadThesisPdf(
            @RequestParam("pdfFile")
            MultipartFile pdfFile,
            Authentication authentication) {

        StudentProfile student =
                getAuthenticatedStudent(
                        authentication
                );


        thesisAssignmentService
                .uploadThesisPdf(
                        student,
                        pdfFile
                );


        return "redirect:/student/assignment";
    }
}