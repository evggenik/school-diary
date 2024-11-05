package com.evggenn.school.teacher.dto;

import com.evggenn.school.role.Role;

import java.time.LocalDate;

public record EditTeacherDto(
        Long personId,
        String userName,
        String email,
        LocalDate birthDate,
        Role role,
        String avatarUrl
) {
}
