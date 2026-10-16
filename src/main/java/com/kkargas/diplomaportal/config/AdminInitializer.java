package com.kkargas.diplomaportal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kkargas.diplomaportal.enums.UserRole;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String adminUsername = "admin";

        if (!userRepository.existsByUsername(adminUsername)) {

            User admin = new User();

            admin.setUsername(adminUsername);
            admin.setPassword(
                    passwordEncoder.encode("admin123")
            );
            admin.setUserRole(UserRole.ADMIN);

            userRepository.save(admin);
        }
    }
}