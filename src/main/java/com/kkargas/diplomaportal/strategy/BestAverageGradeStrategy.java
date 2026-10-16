package com.kkargas.diplomaportal.strategy;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kkargas.diplomaportal.model.ThesisApplication;

@Component
public class BestAverageGradeStrategy
        implements ThesisAssignmentStrategy {


    @Override
    public ThesisApplication selectStudent(
            List<ThesisApplication> applications,
            Double gradeThreshold,
            Integer coursesThreshold) {

        if (applications == null
                || applications.isEmpty()) {

            throw new IllegalArgumentException(
                    "No applications are available for this thesis."
            );
        }


        return applications.stream()

                .filter(application ->
                        application.getStudent()
                                .getAverageGrade() != null)

                .max(
                        Comparator.comparing(
                                application ->
                                        application
                                                .getStudent()
                                                .getAverageGrade()
                        )
                )

                .orElseThrow(() ->
                        new IllegalStateException(
                                "No applicant has an average grade."
                        ));
    }
}