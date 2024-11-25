package com.evggenn.school.teacher.dto;

import com.evggenn.school.person.Person;
import com.evggenn.school.role.Role;

import java.time.LocalDate;


public record NewTeacherDto(
        String firstName,
        String lastName,
        String email,
        String password,
        Person.Gender gender,
        Role role,
        String username,
        LocalDate birthDate,
        String avatarUrl
        ) { }
