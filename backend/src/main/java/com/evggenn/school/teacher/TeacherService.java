package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import com.evggenn.school.person.Person;
import com.evggenn.school.person.PersonRepo;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

//    Если много учителей, возможно, стоит рассмотреть возможность пагинации
//    или фильтрации, чтобы избежать загрузки слишком большого объема данных
//    за один раз.
    public List<TeacherDto> getAllTeacher() {
        List<Teacher> allTeachers = teacherRepo.findAll();
        return teacherMapper.allTeacherDto(allTeachers);
    }

    @Transactional
    public Teacher createTeacher(NewTeacherDto newTeacherDto) {

        Person person = teacherMapper.toPerson(newTeacherDto);

        Person savedPerson = personRepo.save(person);

        Teacher teacher = teacherMapper.toTeacher(newTeacherDto, savedPerson);

        return teacherRepo.save(teacher);
    }

    public void deleteTeacher(Long teacherId) {
        if (!teacherRepo.existsById(teacherId)) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }
        teacherRepo.deleteById(teacherId);
    }

}
