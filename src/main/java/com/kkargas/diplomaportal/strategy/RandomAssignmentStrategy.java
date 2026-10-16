package com.kkargas.diplomaportal.strategy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.kkargas.diplomaportal.model.ThesisApplication;

@Component
public class RandomAssignmentStrategy
        implements ThesisAssignmentStrategy {


    @Override
    public ThesisApplication selectStudent(
            List<ThesisApplication> applications,
            Double gradeThreshold,
            Integer coursesThreshold) {

        validateApplications(applications);


        int randomIndex =
                ThreadLocalRandom.current()
                        .nextInt(applications.size());


        return applications.get(randomIndex);
    }


    private void validateApplications(
            List<ThesisApplication> applications) {

        if (applications == null
                || applications.isEmpty()) {

            throw new IllegalArgumentException(
                    "No applications are available for this thesis."
            );
        }
    }
}