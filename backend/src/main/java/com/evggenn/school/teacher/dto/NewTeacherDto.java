package com.evggenn.school.teacher.dto;

import com.evggenn.school.role.Role;

import java.time.LocalDate;


public record NewTeacherDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String gender,
        Role role,
        String username,
        LocalDate birthDate,
        String avatarUrl
        ) { }
