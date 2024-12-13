package com.evggenn.school.auth;

import com.evggenn.school.teacher.dto.TeacherDto;

public record AuthenticationResponse(
        String token,
        TeacherDto teacherDto) {
}
