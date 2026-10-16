package com.kkargas.diplomaportal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.StudentProfileService;
import com.kkargas.diplomaportal.service.UserService;

@Controller
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;
    private final UserService userService;

    public StudentProfileController(
            StudentProfileService studentProfileService,
            UserService userService) {

        this.studentProfileService = studentProfileService;
        this.userService = userService;
    }

    @GetMapping
    public String getStudentProfile(
            Authentication authentication,
            Model model) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        StudentProfile profile = studentProfileService
                .findByUser(user)
                .orElseGet(() -> {

                    StudentProfile newProfile = new StudentProfile();
                    newProfile.setUser(user);

                    return newProfile;
                });

        model.addAttribute("studentProfile", profile);

        return "student/StudentProfilePage";
    }

    @PostMapping
    public String saveStudentProfile(
            Authentication authentication,
            StudentProfile studentProfile,
            @RequestParam("profilePicture") MultipartFile profilePicture) 
    			throws IOException {
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        StudentProfile existingProfile = studentProfileService
                .findByUser(user)
                .orElse(null);

        if (existingProfile != null) {

            existingProfile.setFullName(studentProfile.getFullName());
            existingProfile.setEmail(studentProfile.getEmail());
            existingProfile.setRegistrationNumber(
                    studentProfile.getRegistrationNumber()
            );
            existingProfile.setAverageGrade(
                    studentProfile.getAverageGrade()
            );
            existingProfile.setRemainingCourses(
                    studentProfile.getRemainingCourses()
            );
            
            if (!profilePicture.isEmpty()) {

	            String fileName =
	                    UUID.randomUUID()
	                    + "-"
	                    + profilePicture.getOriginalFilename();

	            Path uploadDirectory =
	                    Paths.get("uploads/student-profile-pictures");

	            Files.createDirectories(uploadDirectory);

	            Path filePath =
	                    uploadDirectory.resolve(fileName);

	            Files.copy(
	                    profilePicture.getInputStream(),
	                    filePath,
	                    StandardCopyOption.REPLACE_EXISTING
	            );

	            existingProfile.setProfilePicturePath(
	                    "/uploads/student-profile-pictures/" + fileName
	            );
	        }
            

            studentProfileService.save(existingProfile);

        } else {

            studentProfile.setUser(user);
            
            if (!profilePicture.isEmpty()) {

	            String fileName =
	                    UUID.randomUUID()
	                    + "-"
	                    + profilePicture.getOriginalFilename();

	            Path uploadDirectory =
	                    Paths.get("uploads/student-profile-pictures");

	            Files.createDirectories(uploadDirectory);

	            Path filePath =
	                    uploadDirectory.resolve(fileName);

	            Files.copy(
	                    profilePicture.getInputStream(),
	                    filePath,
	                    StandardCopyOption.REPLACE_EXISTING
	            );

	            studentProfile.setProfilePicturePath(
	                    "/uploads/student-profile-pictures/" + fileName
	            );
	        }

            studentProfileService.save(studentProfile);
        }

        return "redirect:/student/profile";
    }
}