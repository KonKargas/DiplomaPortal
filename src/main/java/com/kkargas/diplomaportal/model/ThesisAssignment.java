package com.kkargas.diplomaportal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "thesis_assignments")
public class ThesisAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;


    /*
     * One thesis topic can only be assigned once.
     */
    @OneToOne(optional = false)
    @JoinColumn(
            name = "thesis_id",
            nullable = false,
            unique = true
    )
    private Thesis thesis;


    /*
     * One student can only have one thesis.
     */
    @OneToOne(optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            unique = true
    )
    private StudentProfile student;


    @Column(nullable = false)
    private LocalDateTime assignedAt;


    /*
     * We will use these later for My Thesis.
     */

    private String pdfFilePath;

    private LocalDateTime submittedAt;

    private Double implementationGrade;

    private Double reportGrade;

    private Double presentationGrade;

    private Double finalGrade;


    public ThesisAssignment() {
    }


    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }


    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }


    public StudentProfile getStudent() {
        return student;
    }

    public void setStudent(StudentProfile student) {
        this.student = student;
    }


    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }


    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }


    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }


    public Double getImplementationGrade() {
        return implementationGrade;
    }

    public void setImplementationGrade(Double implementationGrade) {
        this.implementationGrade = implementationGrade;
    }


    public Double getReportGrade() {
        return reportGrade;
    }

    public void setReportGrade(Double reportGrade) {
        this.reportGrade = reportGrade;
    }


    public Double getPresentationGrade() {
        return presentationGrade;
    }

    public void setPresentationGrade(Double presentationGrade) {
        this.presentationGrade = presentationGrade;
    }


    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }
}