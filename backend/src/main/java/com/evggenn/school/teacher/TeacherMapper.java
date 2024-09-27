package com.evggenn.school.teacher;

import com.evggenn.school.person.Person;
import com.evggenn.school.role.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TeacherMapper {

    public TeacherDto teacherDto(Teacher teacher) {
        return new TeacherDto(
                teacher.getFirstName(),
                teacher.getLastName()
        );
    }

    public Person toPerson(NewTeacherDto newTeacherDto) {
        Person person = new Person();
        person.setEmail(newTeacherDto.email());
        person.setPassword(newTeacherDto.password());
        person.setGender(Person.Gender.valueOf(newTeacherDto.gender()));
        person.setRole(Role.valueOf(newTeacherDto.role().toUpperCase()));
        person.setUserName(newTeacherDto.username());
        person.setBirthDate(newTeacherDto.birthDate());
        person.setCreatedAt(LocalDateTime.now());
        return person;
    }

    public Teacher toTeacher(NewTeacherDto newTeacherDto, Person person) {
        Teacher teacher = new Teacher();
        teacher.setPerson(person);
        teacher.setFirstName(newTeacherDto.firstName());
        teacher.setLastName(newTeacherDto.lastName());
        return teacher;
    }

}
