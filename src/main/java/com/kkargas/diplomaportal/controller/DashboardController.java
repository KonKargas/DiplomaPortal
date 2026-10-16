package com.kkargas.diplomaportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isProfessor = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PROFESSOR"));

        boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_STUDENT"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }

        if (isProfessor) {
            return "redirect:/professor/dashboard";
        }

        if (isStudent) {
            return "redirect:/student/dashboard";
        }

        return "redirect:/";
    }
}