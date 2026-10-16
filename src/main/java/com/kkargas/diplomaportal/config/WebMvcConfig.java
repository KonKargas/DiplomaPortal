package com.kkargas.diplomaportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kkargas.diplomaportal.interceptor.ProfessorProfileInterceptor;
import com.kkargas.diplomaportal.interceptor.StudentProfileInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final StudentProfileInterceptor studentProfileInterceptor;
    private final ProfessorProfileInterceptor professorProfileInterceptor;

    public WebMvcConfig(
            StudentProfileInterceptor studentProfileInterceptor,
            ProfessorProfileInterceptor professorProfileInterceptor) {

        this.studentProfileInterceptor = studentProfileInterceptor;
        this.professorProfileInterceptor = professorProfileInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(studentProfileInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns("/student/profile");
        
        registry.addInterceptor(professorProfileInterceptor)
        		.addPathPatterns("/professor/**")
        		.excludePathPatterns("/professor/profile");
    }
    
    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations(
                    "file:uploads/"
            );
    }
}