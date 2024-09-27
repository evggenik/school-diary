package com.evggenn.school.teacher;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/teachers")
@RestController
@AllArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{teacherId}")
    public TeacherDto getTeacher(@PathVariable("teacherId") Long teacherId) {
        return teacherService.getTeacher(teacherId);
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody NewTeacherDto newTeacherDto) {
        Teacher createdTeacher = teacherService.createTeacher(newTeacherDto);
        return ResponseEntity.ok(createdTeacher);
    }

}
