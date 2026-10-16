package com.kkargas.diplomaportal.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "theses")
public class Thesis {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long thesisId;
	
	@ManyToOne(optional = false)
	@JoinColumn(
	        name = "professor_id",
	        nullable = false
	)
	private ProfessorProfile supervisor;
	
	@Column(nullable = false)
	private String thesisTitle;
	
	@Column(nullable = false)
	private String thesisDescription;
	
	@OneToMany(mappedBy = "thesis")
	private List<ThesisApplication> applications;
	
	public Thesis() {}

	public Long getThesisId() {
		return thesisId;
	}

	public void setThesisId(Long thesisId) {
		this.thesisId = thesisId;
	}

	public ProfessorProfile getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(ProfessorProfile supervisor) {
		this.supervisor = supervisor;
	}

	public String getThesisTitle() {
		return thesisTitle;
	}

	public void setThesisTitle(String thesisTitle) {
		this.thesisTitle = thesisTitle;
	}

	public String getThesisDescription() {
		return thesisDescription;
	}

	public void setThesisDescription(String thesisDescription) {
		this.thesisDescription = thesisDescription;
	}

	public List<ThesisApplication> getApplications() {
		return applications;
	}

	public void setApplications(List<ThesisApplication> applications) {
		this.applications = applications;
	}
	
}
