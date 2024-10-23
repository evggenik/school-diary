package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import com.evggenn.school.person.PersonService;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/teachers")
@RestController
@AllArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final PersonService personService;

    @GetMapping("/{teacherId}")
    public TeacherDto getTeacher(@PathVariable("teacherId") Long teacherId) {
        return teacherService.getTeacher(teacherId);
    }

    @GetMapping()
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        List<TeacherDto> allTeachers = teacherService.getAllTeacher();
        return ResponseEntity.ok(allTeachers);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Teacher> createTeacher(@RequestPart NewTeacherDto newTeacherDto,
                                                 @RequestPart(value = "avatarFile", required = false)
                                                 MultipartFile avatarFile) throws IOException {
        Teacher createdTeacher = teacherService.createTeacher(newTeacherDto, avatarFile);
        return ResponseEntity.ok(createdTeacher);
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable("teacherId") Long teacherId) {
        try {
            teacherService.deleteTeacher(teacherId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping("/uploadAvatar/{personId}")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long personId, @RequestParam("file") MultipartFile file) {
        try {
            String filePath = personService.uploadAvatar(personId, file);
            return ResponseEntity.ok("Avatar uploaded successfully: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }

}
