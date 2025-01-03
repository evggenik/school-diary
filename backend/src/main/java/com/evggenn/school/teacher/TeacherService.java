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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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

//    временно ограничим вывод первыми 20 пользователями...
    public List<TeacherDto> getAllTeacher() {
        Page<Teacher> page = teacherRepo.findAll(Pageable.ofSize(20));
        return teacherMapper.allTeacherDto(page.getContent());
    }

    @Transactional
    public TeacherDto createTeacher(NewTeacherDto newTeacherDto, MultipartFile avatarFile) throws IOException {

        Person person = teacherMapper.toPerson(newTeacherDto);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person savedPerson = personRepo.save(person);

        updateAvatarIfPresent(person, avatarFile);

        Teacher teacher = teacherMapper.toTeacher(newTeacherDto, savedPerson);

        teacherRepo.save(teacher);
        return teacherMapper.teacherDto(teacher);
    }

    public void deleteTeacher(Long teacherId) {
//        if (!teacherRepo.existsById(teacherId)) {
//            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
//        }

        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        if (teacher.getPerson() != null) {
            personService.deleteAvatar(teacher.getPerson().getId());
        }

        teacherRepo.deleteById(teacherId);
    }

    public TeacherDto editTeacher(long teacherId,
                                  EditTeacherDto editTeacherDto,
                                  MultipartFile avatarFile) throws IOException {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "teacher with id [%s] not found".formatted(teacherId)));

        Person editPerson = teacherMapper.toEditPerson(teacher.getPerson(), editTeacherDto);

        updateAvatarIfPresent(editPerson, avatarFile);

        personRepo.save(editPerson);
        return teacherMapper.teacherDto(teacher);
    }

}
