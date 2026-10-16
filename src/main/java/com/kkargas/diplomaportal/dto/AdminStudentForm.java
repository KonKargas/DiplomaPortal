package com.kkargas.diplomaportal.dto;

public class AdminStudentForm {

    private Long userId;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String registrationNumber;

    private Double averageGrade;

    private Integer remainingCourses;
    
    public AdminStudentForm() {}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Double getAverageGrade() {
		return averageGrade;
	}

	public void setAverageGrade(Double averageGrade) {
		this.averageGrade = averageGrade;
	}

	public Integer getRemainingCourses() {
		return remainingCourses;
	}

	public void setRemainingCourses(Integer remainingCourses) {
		this.remainingCourses = remainingCourses;
	}
    
}