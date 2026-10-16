package com.kkargas.diplomaportal.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.StudentProfileService;
import com.kkargas.diplomaportal.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StudentProfileInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final StudentProfileService studentProfileService;

    public StudentProfileInterceptor(
            UserService userService,
            StudentProfileService studentProfileService) {

        this.userService = userService;
        this.studentProfileService = studentProfileService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        Authentication authentication =
                (Authentication) request.getUserPrincipal();

        if (authentication == null) {
            return true;
        }

        User user = userService.findByUsername(
                authentication.getName()
        );

        if (!studentProfileService.profileExists(user)) {

            response.sendRedirect(
                    request.getContextPath() + "/student/profile"
            );

            return false;
        }

        return true;
    }
}