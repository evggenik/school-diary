package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import com.evggenn.school.person.Person;
import com.evggenn.school.person.PersonRepo;
import com.evggenn.school.person.PersonService;
import com.evggenn.school.teacher.dto.EditTeacherDto;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Data
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final PersonRepo personRepo;
    private final TeacherMapper teacherMapper;
    private final PersonService personService;

    private void updateAvatarIfPresent(Person person,
                                       MultipartFile avatarFile) throws IOException {
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = personService.uploadAvatar(person.getId(), avatarFile);
            person.setAvatarUrl(avatarPath);
        }
    }

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
    public TeacherDto createTeacher(NewTeacherDto newTeacherDto, MultipartFile avatarFile) throws IOException {

        Person person = teacherMapper.toPerson(newTeacherDto);
        Person savedPerson = personRepo.save(person);

        updateAvatarIfPresent(person, avatarFile);

        Teacher teacher = teacherMapper.toTeacher(newTeacherDto, savedPerson);

        teacherRepo.save(teacher);
        return teacherMapper.teacherDto(teacher);
    }

    public void deleteTeacher(Long teacherId) {
        if (!teacherRepo.existsById(teacherId)) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }
        teacherRepo.deleteById(teacherId);
    }

    public TeacherDto editTeacher(long teacherId,
                                  EditTeacherDto editTeacherDto,
                                  MultipartFile avatarFile) throws IOException {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "teacher with id [%s] not found".formatted(teacherId)));

        Person person = teacherMapper.toEditPerson(teacher.getPerson(), editTeacherDto);

        updateAvatarIfPresent(person, avatarFile);

        personRepo.save(person);
        return teacherMapper.teacherDto(teacher);
    }

}
