package com.kkargas.diplomaportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kkargas.diplomaportal.service.AdminUserService;

@Controller
public class AdminDashboardController {

    private final AdminUserService adminUserService;


    public AdminDashboardController(
            AdminUserService adminUserService) {

        this.adminUserService =
                adminUserService;
    }


    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(
            Model model) {

        model.addAttribute(
                "students",
                adminUserService.getStudents()
        );

        model.addAttribute(
                "professors",
                adminUserService.getProfessors()
        );


        return "dashboard/AdminDashboard";
    }
}