package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFound;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class TeacherService {

    private final TeacherRepo teacherRepo;

    public Teacher getTeacher(Long id) {
        return teacherRepo.findById(id)
                .orElseThrow(()->new ResourceNotFound(
                        "teacher with id [%s] not found".formatted(id)));
    }

}
