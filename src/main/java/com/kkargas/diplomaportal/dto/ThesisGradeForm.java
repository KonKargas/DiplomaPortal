package com.kkargas.diplomaportal.dto;

public class ThesisGradeForm {

    private Double implementationGrade;
    private Double reportGrade;
    private Double presentationGrade;


    public ThesisGradeForm() {
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
}