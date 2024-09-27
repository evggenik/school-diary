package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import com.evggenn.school.person.Person;
import com.evggenn.school.person.PersonRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Data
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final PersonRepo personRepo;
    private final TeacherMapper teacherMapper;

    public TeacherDto getTeacher(Long id) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "teacher with id [%s] not found".formatted(id)));
        return teacherMapper.teacherDto(teacher);
    }

    @Transactional
    public Teacher createTeacher(NewTeacherDto newTeacherDto) {
        // Преобразование DTO в Person
        Person person = teacherMapper.toPerson(newTeacherDto);

        // Сохранение объекта Person в базе данных
        Person savedPerson = personRepo.save(person);

        // Преобразование DTO в Teacher с использованием personId
        Teacher teacher = teacherMapper.toTeacher(newTeacherDto, savedPerson);

        // Сохранение объекта Teacher в базе данных
        return teacherRepo.save(teacher);
    }

}
