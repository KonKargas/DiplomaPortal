package com.kkargas.diplomaportal.strategy;

import org.springframework.stereotype.Component;

import com.kkargas.diplomaportal.enums.AssignmentStrategyType;

@Component
public class ThesisAssignmentStrategyFactory {

    private final RandomAssignmentStrategy randomStrategy;

    private final BestAverageGradeStrategy bestAverageGradeStrategy;

    private final FewestRemainingCoursesStrategy fewestRemainingCoursesStrategy;

    private final ThresholdAssignmentStrategy thresholdStrategy;


    public ThesisAssignmentStrategyFactory(
            RandomAssignmentStrategy randomStrategy,
            BestAverageGradeStrategy bestAverageGradeStrategy,
            FewestRemainingCoursesStrategy fewestRemainingCoursesStrategy,
            ThresholdAssignmentStrategy thresholdStrategy) {

        this.randomStrategy =
                randomStrategy;

        this.bestAverageGradeStrategy =
                bestAverageGradeStrategy;

        this.fewestRemainingCoursesStrategy =
                fewestRemainingCoursesStrategy;

        this.thresholdStrategy =
                thresholdStrategy;
    }


    public ThesisAssignmentStrategy getStrategy(
            AssignmentStrategyType type) {

        return switch (type) {

            case RANDOM ->
                    randomStrategy;

            case BEST_AVERAGE_GRADE ->
                    bestAverageGradeStrategy;

            case FEWEST_REMAINING_COURSES ->
                    fewestRemainingCoursesStrategy;

            case THRESHOLD ->
                    thresholdStrategy;
        };
    }
}