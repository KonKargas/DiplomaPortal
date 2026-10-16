package com.kkargas.diplomaportal.enums;

public enum UserRole {
	
	ADMIN("Admin"),
	PROFESSOR("Professor"),
    STUDENT("Student");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
