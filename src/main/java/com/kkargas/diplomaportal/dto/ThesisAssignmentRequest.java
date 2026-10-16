package com.kkargas.diplomaportal.dto;

import com.kkargas.diplomaportal.enums.AssignmentStrategyType;

public class ThesisAssignmentRequest {

    private AssignmentStrategyType strategyType;

    private Double gradeThreshold;

    private Integer coursesThreshold;


    public ThesisAssignmentRequest() {
    }


    public AssignmentStrategyType getStrategyType() {
        return strategyType;
    }


    public void setStrategyType(
            AssignmentStrategyType strategyType) {

        this.strategyType = strategyType;
    }


    public Double getGradeThreshold() {
        return gradeThreshold;
    }


    public void setGradeThreshold(
            Double gradeThreshold) {

        this.gradeThreshold = gradeThreshold;
    }


    public Integer getCoursesThreshold() {
        return coursesThreshold;
    }


    public void setCoursesThreshold(
            Integer coursesThreshold) {

        this.coursesThreshold = coursesThreshold;
    }
}