package com.evggenn.school.teacher;

import java.time.LocalDate;

public record NewTeacherDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String gender,
        String role,
        String username,
        LocalDate birthDate
) {
}