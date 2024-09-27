package com.evggenn.school.teacher;

import java.time.LocalDate;
import java.util.Optional;

public record NewTeacherDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String gender,
        String role,
        String username,
        LocalDate birthDate

//        Optional<Integer> age
) {
}
