package com.kkargas.diplomaportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kkargas.diplomaportal.dto.AdminStudentForm;
import com.kkargas.diplomaportal.service.AdminUserService;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final AdminUserService adminUserService;


    public AdminStudentController(
            AdminUserService adminUserService) {

        this.adminUserService =
                adminUserService;
    }


    @GetMapping("/create")
    public String createStudentPage(
            Model model) {

        model.addAttribute(
                "studentForm",
                new AdminStudentForm()
        );

        model.addAttribute(
                "editMode",
                false
        );

        return "admin/StudentFormPage";
    }


    @PostMapping("/create")
    public String createStudent(
            @ModelAttribute("studentForm")
            AdminStudentForm form) {

        adminUserService.createStudent(form);

        return "redirect:/admin/dashboard";
    }


    @GetMapping("/{userId}/edit")
    public String editStudentPage(
            @PathVariable Long userId,
            Model model) {

        model.addAttribute(
                "studentForm",
                adminUserService
                        .getStudentForm(userId)
        );

        model.addAttribute(
                "editMode",
                true
        );

        return "admin/StudentFormPage";
    }


    @PostMapping("/{userId}/edit")
    public String editStudent(
            @PathVariable Long userId,
            @ModelAttribute("studentForm")
            AdminStudentForm form) {

        adminUserService.updateStudent(
                userId,
                form
        );

        return "redirect:/admin/dashboard";
    }


    @PostMapping("/{userId}/delete")
    public String deleteStudent(
            @PathVariable Long userId) {

        adminUserService.deleteStudent(userId);

        return "redirect:/admin/dashboard";
    }
}