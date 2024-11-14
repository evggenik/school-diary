package com.evggenn.school.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMINISTRATOR,
    TEACHER,
    STUDENT,
    PARENT;

    @JsonCreator
    public static Role fromString(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
