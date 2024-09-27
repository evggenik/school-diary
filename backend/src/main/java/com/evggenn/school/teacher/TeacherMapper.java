package com.evggenn.school.teacher;

import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherDto TeacherDto(Teacher teacher) {
        return new TeacherDto(
                teacher.getFirstName(),
                teacher.getLastName()
        );
    }

}
