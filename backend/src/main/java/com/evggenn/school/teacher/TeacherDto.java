package com.evggenn.school.teacher;

import java.time.LocalDate;
import java.util.Optional;

public record TeacherDto(
        String firstName,
        String lastName
//        Optional<String> email,
//        String password,
//        String gender,
//        String role,
//        String username,
//        LocalDate localDate
) { }
