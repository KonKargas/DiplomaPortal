package com.kkargas.diplomaportal.strategy;

import java.util.List;

import com.kkargas.diplomaportal.model.ThesisApplication;

public interface ThesisAssignmentStrategy {

    ThesisApplication selectStudent(
            List<ThesisApplication> applications,
            Double gradeThreshold,
            Integer coursesThreshold
    );
}