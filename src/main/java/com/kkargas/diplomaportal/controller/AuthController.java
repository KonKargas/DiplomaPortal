package com.kkargas.diplomaportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kkargas.diplomaportal.dto.RegisterRequest;
import com.kkargas.diplomaportal.enums.UserRole;
import com.kkargas.diplomaportal.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    private static final UserRole[] REGISTER_ROLES = {
            UserRole.PROFESSOR,
            UserRole.STUDENT
    };

    @GetMapping("/")
    public String getLoginPage() {
        return "auth/LoginPage";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {

        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }

        model.addAttribute("roles", REGISTER_ROLES);

        return "auth/RegisterPage";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "password.mismatch",
                    "Passwords do not match."
            );
        }

        if (userService.usernameExists(request.getUsername())) {
            bindingResult.rejectValue(
                    "username",
                    "username.exists",
                    "This username is already registered."
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", REGISTER_ROLES);
            return "auth/RegisterPage";
        }

        userService.registerUser(request);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registration completed successfully. You can now log in."
        );

        return "redirect:/";
    }
}