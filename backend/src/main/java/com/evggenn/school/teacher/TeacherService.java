package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final TeacherMapper teacherMapper;

    public TeacherDto getTeacher(Long id) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "teacher with id [%s] not found".formatted(id)));
        return teacherMapper.TeacherDto(teacher);
    }

}
