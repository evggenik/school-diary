package com.evggenn.school.teacher.dto;


import com.evggenn.school.person.Person;
import com.evggenn.school.role.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TeacherDto(
        Long id,
        String firstName,
        String lastName,
        String userName,
        String email,
        LocalDate birthDate,
        Person.Gender gender,
        Role role,
        LocalDateTime createdAt

) { }
