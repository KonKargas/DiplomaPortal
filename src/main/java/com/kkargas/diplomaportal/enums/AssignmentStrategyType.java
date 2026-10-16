package com.kkargas.diplomaportal.enums;

public enum AssignmentStrategyType {

    RANDOM("Random Choice"),

    BEST_AVERAGE_GRADE("Best Average Grade"),

    FEWEST_REMAINING_COURSES("Fewest Remaining Courses"),

    THRESHOLD("Grade & Remaining Courses Threshold");


    private final String label;


    AssignmentStrategyType(String label) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }
}