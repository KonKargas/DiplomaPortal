package com.kkargas.diplomaportal.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kkargas.diplomaportal.dto.RegisterRequest;
import com.kkargas.diplomaportal.enums.UserRole;
import com.kkargas.diplomaportal.model.User;
import com.kkargas.diplomaportal.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(RegisterRequest request) {

        String username = request.getUsername().trim();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        User user = new User(
                username,
                passwordEncoder.encode(request.getPassword()),
                request.getUserRole()
        );

        return userRepository.save(user);
    }


    public boolean usernameExists(String username) {
        return userRepository
                .existsByUsername(username.trim());
    }


    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found."
                        ));
    }

    public User findById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found."
                        ));
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByUserRole(role);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void changePassword(
            User user,
            String rawPassword) {

        user.setPassword(
                passwordEncoder.encode(rawPassword)
        );

        userRepository.save(user);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}