package com.evggenn.school.teacher;

import com.evggenn.school.person.Person;
import com.evggenn.school.teacher.dto.EditTeacherDto;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TeacherMapper {

    public TeacherDto teacherDto(Teacher teacher) {
        Person person = teacher.getPerson();
        return new TeacherDto(
                teacher.getId(),
                person.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                person.getUserName(),
                person.getEmail(),
                person.getBirthDate(),
                person.getGender(),
                person.getRole(),
                person.getCreatedAt(),
                person.getAvatarUrl()
        );
    }

    public List<TeacherDto> allTeacherDto(List<Teacher> teachers) {
        return teachers.stream().map(this::teacherDto).toList();
    }

    public Person toPerson(NewTeacherDto newTeacherDto) {
        Person person = new Person();
        person.setEmail(newTeacherDto.email());
        person.setPassword(newTeacherDto.password());
        person.setGender(newTeacherDto.gender());
        person.setRole(newTeacherDto.role());
        person.setUserName(newTeacherDto.username());
        person.setBirthDate(newTeacherDto.birthDate());
        person.setCreatedAt(LocalDateTime.now());
        if (newTeacherDto.avatarUrl() != null && !newTeacherDto.avatarUrl().isEmpty()) {
            person.setAvatarUrl(newTeacherDto.avatarUrl());
        }
//        newTeacherDto.age().ifPresent(person::setAge);
        return person;
    }

    public Teacher toTeacher(NewTeacherDto newTeacherDto, Person person) {
        Teacher teacher = new Teacher();
        teacher.setPerson(person);
        teacher.setFirstName(newTeacherDto.firstName());
        teacher.setLastName(newTeacherDto.lastName());
        return teacher;
    }

    public Person toEditPerson(Person person, EditTeacherDto editTeacherDto) {
        person.setUserName(editTeacherDto.userName());
        person.setEmail(editTeacherDto.email());
        person.setBirthDate(editTeacherDto.birthDate());
        person.setRole(editTeacherDto.role());
        person.setAvatarUrl(editTeacherDto.avatarUrl());
        return person;
    }

}
