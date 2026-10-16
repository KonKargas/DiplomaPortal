package com.kkargas.diplomaportal.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.ProfessorProfileService;
import com.kkargas.diplomaportal.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ProfessorProfileInterceptor implements HandlerInterceptor{
	
	private final UserService userService;
    private final ProfessorProfileService professorProfileService;

    public ProfessorProfileInterceptor(
            UserService userService,
            ProfessorProfileService professorProfileService) {

        this.userService = userService;
        this.professorProfileService = professorProfileService;
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

        if (!professorProfileService.profileExists(user)) {

            response.sendRedirect(
                    request.getContextPath() + "/professor/profile"
            );

            return false;
        }

        return true;
    }

}
