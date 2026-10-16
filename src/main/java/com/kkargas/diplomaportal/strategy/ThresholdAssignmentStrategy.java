package com.kkargas.diplomaportal.strategy;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kkargas.diplomaportal.model.ThesisApplication;

@Component
public class ThresholdAssignmentStrategy
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


        if (gradeThreshold == null
                || coursesThreshold == null) {

            throw new IllegalArgumentException(
                    "Both thresholds are required."
            );
        }


        return applications.stream()

                .filter(application -> {

                    Double grade =
                            application
                                    .getStudent()
                                    .getAverageGrade();

                    Integer remainingCourses =
                            application
                                    .getStudent()
                                    .getRemainingCourses();


                    return grade != null
                            && remainingCourses != null
                            && grade > gradeThreshold
                            && remainingCourses < coursesThreshold;
                })


                /*
                 * If multiple students satisfy
                 * the thresholds, choose the one
                 * with the highest average grade.
                 */

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
                                "No student satisfies the selected thresholds."
                        ));
    }
}