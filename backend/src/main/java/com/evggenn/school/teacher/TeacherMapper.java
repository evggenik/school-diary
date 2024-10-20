package com.evggenn.school.teacher;

import com.evggenn.school.person.Person;
import com.evggenn.school.role.Role;
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
                teacher.getFirstName(),
                teacher.getLastName(),
                person.getUserName(),
                person.getEmail(),
                person.getBirthDate(),
                person.getGender(),
                person.getRole(),
                person.getCreatedAt()
        );
    }

    public List<TeacherDto> allTeacherDto(List<Teacher> teachers) {
        return teachers.stream().map(this::teacherDto).toList();
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

}
