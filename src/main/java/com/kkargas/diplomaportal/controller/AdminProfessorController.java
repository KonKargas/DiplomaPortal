package com.kkargas.diplomaportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kkargas.diplomaportal.dto.AdminProfessorForm;
import com.kkargas.diplomaportal.service.AdminUserService;

@Controller
@RequestMapping("/admin/professors")
public class AdminProfessorController {

    private final AdminUserService adminUserService;


    public AdminProfessorController(
            AdminUserService adminUserService) {

        this.adminUserService =
                adminUserService;
    }


    @GetMapping("/create")
    public String createProfessorPage(
            Model model) {

        model.addAttribute(
                "professorForm",
                new AdminProfessorForm()
        );

        model.addAttribute(
                "editMode",
                false
        );

        return "admin/ProfessorFormPage";
    }


    @PostMapping("/create")
    public String createProfessor(
            @ModelAttribute("professorForm")
            AdminProfessorForm form) {

        adminUserService
                .createProfessor(form);

        return "redirect:/admin/dashboard";
    }


    @GetMapping("/{userId}/edit")
    public String editProfessorPage(
            @PathVariable Long userId,
            Model model) {

        model.addAttribute(
                "professorForm",
                adminUserService
                        .getProfessorForm(userId)
        );

        model.addAttribute(
                "editMode",
                true
        );

        return "admin/ProfessorFormPage";
    }


    @PostMapping("/{userId}/edit")
    public String editProfessor(
            @PathVariable Long userId,
            @ModelAttribute("professorForm")
            AdminProfessorForm form) {

        adminUserService.updateProfessor(
                userId,
                form
        );

        return "redirect:/admin/dashboard";
    }


    @PostMapping("/{userId}/delete")
    public String deleteProfessor(
            @PathVariable Long userId) {

        adminUserService
                .deleteProfessor(userId);

        return "redirect:/admin/dashboard";
    }
}