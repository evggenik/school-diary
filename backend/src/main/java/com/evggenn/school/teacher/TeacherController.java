package com.evggenn.school.teacher;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/teachers")
@RestController
@AllArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{teacherId}")
    public TeacherDto getTeacher(@PathVariable("teacherId") Long teacherId) {
        return teacherService.getTeacher(teacherId);
    }

}
