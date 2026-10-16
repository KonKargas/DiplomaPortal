package com.kkargas.diplomaportal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkargas.diplomaportal.dto.AdminProfessorForm;
import com.kkargas.diplomaportal.dto.AdminStudentForm;
import com.kkargas.diplomaportal.enums.UserRole;
import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.StudentProfile;
import com.kkargas.diplomaportal.model.User;

@Service
public class AdminUserService {

    private final UserService userService;
    private final StudentProfileService studentProfileService;
    private final ProfessorProfileService professorProfileService;


    public AdminUserService(
            UserService userService,
            StudentProfileService studentProfileService,
            ProfessorProfileService professorProfileService) {

        this.userService = userService;
        this.studentProfileService = studentProfileService;
        this.professorProfileService =
                professorProfileService;
    }


    /* ========================================
       STUDENTS
    ======================================== */

    public List<AdminStudentForm> getStudents() {

        List<User> users =
                userService.findByRole(UserRole.STUDENT);

        List<AdminStudentForm> students =
                new ArrayList<>();

        for (User user : users) {

            AdminStudentForm dto =
                    toStudentDto(user);

            students.add(dto);
        }

        return students;
    }


    public AdminStudentForm getStudentForm(
            Long userId) {

        User user =
                userService.findById(userId);

        validateRole(
                user,
                UserRole.STUDENT
        );

        return toStudentDto(user);
    }


    @Transactional
    public void createStudent(
            AdminStudentForm form) {

        if (userService.usernameExists(
                form.getUsername())) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }


        User user =
                new User(
                        form.getUsername().trim(),
                        userService.encodePassword(
                                form.getPassword()
                        ),
                        UserRole.STUDENT
                );

        userService.save(user);


        StudentProfile profile =
                new StudentProfile();

        profile.setUser(user);

        profile.setFullName(
                form.getFullName()
        );

        profile.setEmail(
                form.getEmail()
        );

        profile.setRegistrationNumber(
                form.getRegistrationNumber()
        );

        profile.setAverageGrade(
                form.getAverageGrade()
        );

        profile.setRemainingCourses(
                form.getRemainingCourses()
        );

        studentProfileService.save(profile);
    }


    @Transactional
    public void updateStudent(
            Long userId,
            AdminStudentForm form) {

        User user =
                userService.findById(userId);

        validateRole(
                user,
                UserRole.STUDENT
        );


        if (!user.getUsername()
                .equals(form.getUsername().trim())
                && userService.usernameExists(
                        form.getUsername())) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }


        user.setUsername(
                form.getUsername().trim()
        );


        if (form.getPassword() != null
                && !form.getPassword()
                        .isBlank()) {

            user.setPassword(
                    userService.encodePassword(
                            form.getPassword()
                    )
            );
        }


        userService.save(user);


        StudentProfile profile =
                studentProfileService
                        .findByUser(user)
                        .orElseGet(() -> {

                            StudentProfile p =
                                    new StudentProfile();

                            p.setUser(user);

                            return p;
                        });


        profile.setFullName(
                form.getFullName()
        );

        profile.setEmail(
                form.getEmail()
        );

        profile.setRegistrationNumber(
                form.getRegistrationNumber()
        );

        profile.setAverageGrade(
                form.getAverageGrade()
        );

        profile.setRemainingCourses(
                form.getRemainingCourses()
        );


        studentProfileService.save(profile);
    }


    @Transactional
    public void deleteStudent(Long userId) {

        User user =
                userService.findById(userId);

        validateRole(
                user,
                UserRole.STUDENT
        );

        studentProfileService.deleteByUser(user);

        userService.deleteUser(user);
    }



    /* ========================================
       PROFESSORS
    ======================================== */

    public List<AdminProfessorForm> getProfessors() {

        List<User> users =
                userService.findByRole(
                        UserRole.PROFESSOR
                );

        List<AdminProfessorForm> professors =
                new ArrayList<>();


        for (User user : users) {

            professors.add(
                    toProfessorDto(user)
            );
        }

        return professors;
    }


    public AdminProfessorForm getProfessorForm(
            Long userId) {

        User user =
                userService.findById(userId);

        validateRole(
                user,
                UserRole.PROFESSOR
        );

        return toProfessorDto(user);
    }


    @Transactional
    public void createProfessor(
            AdminProfessorForm form) {

        if (userService.usernameExists(
                form.getUsername())) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }


        User user =
                new User(
                        form.getUsername().trim(),
                        userService.encodePassword(
                                form.getPassword()
                        ),
                        UserRole.PROFESSOR
                );

        userService.save(user);


        ProfessorProfile profile =
                new ProfessorProfile();

        profile.setUser(user);

        profile.setFullName(
                form.getFullName()
        );

        profile.setEmail(
                form.getEmail()
        );

        profile.setDepartment(
                form.getDepartment()
        );

        profile.setOffice(
                form.getOffice()
        );

        profile.setFieldOfStudies(
                form.getFieldOfStudies()
        );


        professorProfileService.save(profile);
    }


    @Transactional
    public void updateProfessor(
            Long userId,
            AdminProfessorForm form) {

        User user =
                userService.findById(userId);

        validateRole(
                user,
                UserRole.PROFESSOR
        );


        if (!user.getUsername()
                .equals(form.getUsername().trim())
                && userService.usernameExists(
                        form.getUsername())) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }


        user.setUsername(
                form.getUsername().trim()
        );


        if (form.getPassword() != null
                && !form.getPassword()
                        .isBlank()) {

            user.setPassword(
                    userService.encodePassword(
                            form.getPassword()
                    )
            );
        }


        userService.save(user);


        ProfessorProfile profile =
                professorProfileService
                        .findByUser(user)
                        .orElseGet(() -> {

                            ProfessorProfile p =
                                    new ProfessorProfile();

                            p.setUser(user);

                            return p;
                        });


        profile.setFullName(
                form.getFullName()
        );

        profile.setEmail(
                form.getEmail()
        );

        profile.setDepartment(
                form.getDepartment()
        );

        profile.setOffice(
                form.getOffice()
        );

        profile.setFieldOfStudies(
                form.getFieldOfStudies()
        );


        professorProfileService.save(profile);
    }


    @Transactional
    public void deleteProfessor(
            Long userId) {

        User user =
                userService.findById(userId);

        validateRole(
                user,
                UserRole.PROFESSOR
        );

        professorProfileService.deleteByUser(user);

        userService.deleteUser(user);
    }



    /* ========================================
       DTO MAPPING
    ======================================== */

    private AdminStudentForm toStudentDto(
            User user) {

        AdminStudentForm dto =
                new AdminStudentForm();

        dto.setUserId(
                user.getUserId()
        );

        dto.setUsername(
                user.getUsername()
        );


        studentProfileService
                .findByUser(user)
                .ifPresent(profile -> {

                    dto.setFullName(
                            profile.getFullName()
                    );

                    dto.setEmail(
                            profile.getEmail()
                    );

                    dto.setRegistrationNumber(
                            profile.getRegistrationNumber()
                    );

                    dto.setAverageGrade(
                            profile.getAverageGrade()
                    );

                    dto.setRemainingCourses(
                            profile.getRemainingCourses()
                    );
                });


        return dto;
    }


    private AdminProfessorForm toProfessorDto(
            User user) {

        AdminProfessorForm dto =
                new AdminProfessorForm();

        dto.setUserId(
                user.getUserId()
        );

        dto.setUsername(
                user.getUsername()
        );


        professorProfileService
                .findByUser(user)
                .ifPresent(profile -> {

                    dto.setFullName(
                            profile.getFullName()
                    );

                    dto.setEmail(
                            profile.getEmail()
                    );

                    dto.setDepartment(
                            profile.getDepartment()
                    );

                    dto.setOffice(
                            profile.getOffice()
                    );

                    dto.setFieldOfStudies(
                            profile.getFieldOfStudies()
                    );
                });


        return dto;
    }


    private void validateRole(
            User user,
            UserRole expectedRole) {

        if (user.getUserRole()
                != expectedRole) {

            throw new IllegalArgumentException(
                    "Invalid user role."
            );
        }
    }
}