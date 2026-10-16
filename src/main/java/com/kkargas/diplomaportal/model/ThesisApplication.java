package com.kkargas.diplomaportal.model;

import java.time.LocalDateTime;

import com.kkargas.diplomaportal.enums.ApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "thesis_applications",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"thesis_id", "student_id"}
        )
    }
)
public class ThesisApplication {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long applicationId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	private StudentProfile student;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "thesis_id", nullable = false)
	private Thesis thesis;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status;
	
	@Column(length = 1000)
	private String notes;
	
	@Column(nullable = false)
	private LocalDateTime applicationDate;
	
	public ThesisApplication() {}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public StudentProfile getStudent() {
		return student;
	}

	public void setStudent(StudentProfile student) {
		this.student = student;
	}

	public Thesis getThesis() {
		return thesis;
	}

	public void setThesis(Thesis thesis) {
		this.thesis = thesis;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(LocalDateTime applicationDate) {
		this.applicationDate = applicationDate;
	}
	
}
