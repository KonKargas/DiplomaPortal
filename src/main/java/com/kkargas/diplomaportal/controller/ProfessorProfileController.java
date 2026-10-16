package com.kkargas.diplomaportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.service.ProfessorProfileService;
import com.kkargas.diplomaportal.service.UserService;

@Controller
@RequestMapping("/professor/profile")
public class ProfessorProfileController {
	
	private final ProfessorProfileService professorProfileService;
	private final UserService userService;
	
	public ProfessorProfileController (ProfessorProfileService professorProfileService, UserService userService){
		this.professorProfileService = professorProfileService;
		this.userService = userService;
	}
	
	@GetMapping
    public String getProfessorProfile(
            Authentication authentication,
            Model model) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        ProfessorProfile profile = professorProfileService
                .findByUser(user)
                .orElseGet(() -> {

                    ProfessorProfile newProfile = new ProfessorProfile();
                    newProfile.setUser(user);

                    return newProfile;
                });

        model.addAttribute("professorProfile", profile);

        return "professor/ProfessorProfilePage";
    }
	
	@PostMapping
	public String saveProfessorProfile(
	        Authentication authentication,
	        ProfessorProfile professorProfile,
	        @RequestParam("profilePicture") MultipartFile profilePicture)
	        throws IOException {

	    String username = authentication.getName();

	    User user = userService.findByUsername(username);

	    ProfessorProfile existingProfile = professorProfileService
	            .findByUser(user)
	            .orElse(null);

	    if (existingProfile != null) {

	        existingProfile.setFullName(
	                professorProfile.getFullName()
	        );

	        existingProfile.setEmail(
	                professorProfile.getEmail()
	        );

	        existingProfile.setDepartment(
	                professorProfile.getDepartment()
	        );

	        existingProfile.setOffice(
	                professorProfile.getOffice()
	        );

	        existingProfile.setFieldOfStudies(
	                professorProfile.getFieldOfStudies()
	        );


	        if (!profilePicture.isEmpty()) {

	            String fileName =
	                    UUID.randomUUID()
	                    + "-"
	                    + profilePicture.getOriginalFilename();

	            Path uploadDirectory =
	                    Paths.get("uploads/professor-profile-pictures");

	            Files.createDirectories(uploadDirectory);

	            Path filePath =
	                    uploadDirectory.resolve(fileName);

	            Files.copy(
	                    profilePicture.getInputStream(),
	                    filePath,
	                    StandardCopyOption.REPLACE_EXISTING
	            );

	            existingProfile.setProfilePicturePath(
	                    "/uploads/professor-profile-pictures/" + fileName
	            );
	        }


	        professorProfileService.save(existingProfile);

	    } else {

	        professorProfile.setUser(user);


	        if (!profilePicture.isEmpty()) {

	            String fileName =
	                    UUID.randomUUID()
	                    + "-"
	                    + profilePicture.getOriginalFilename();

	            Path uploadDirectory =
	                    Paths.get("uploads/professor-profile-pictures");

	            Files.createDirectories(uploadDirectory);

	            Path filePath =
	                    uploadDirectory.resolve(fileName);

	            Files.copy(
	                    profilePicture.getInputStream(),
	                    filePath,
	                    StandardCopyOption.REPLACE_EXISTING
	            );

	            professorProfile.setProfilePicturePath(
	                    "/uploads/professor-profile-pictures/" + fileName
	            );
	        }


	        professorProfileService.save(professorProfile);
	    }


	    return "redirect:/professor/profile";
	}
	
}
