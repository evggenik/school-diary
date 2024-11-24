package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import com.evggenn.school.person.Person;
import com.evggenn.school.person.PersonRepo;
import com.evggenn.school.person.PersonService;
import com.evggenn.school.role.Role;
import com.evggenn.school.teacher.dto.TeacherDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TeacherServiceTest {

    private TeacherService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private TeacherRepo teacherRepo;
    @Mock
    private PersonRepo personRepo;
    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private PersonService personService;

    private Person person1;
    private Person person2;
    private Teacher teacher1;
    private Teacher teacher2;
    private List<Teacher> teacherList;
    private List<TeacherDto> expectedTeacherDtoList;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TeacherService(teacherRepo, personRepo,
                teacherMapper, personService);

        person1 = new Person();
        person1.setId(1L);
        person1.setUserName("person1");
        person1.setEmail("person1@mail.foo");
        person1.setBirthDate(LocalDate.of(1990, 10, 10));
        person1.setGender(Person.Gender.MALE);
        person1.setRole(Role.STUDENT);
        person1.setCreatedAt(LocalDateTime.of(1990, 10, 10, 10, 10));
        person1.setAvatarUrl("uploads/person1_1/avatar1.jpg");

        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Wasya");
        teacher1.setLastName("Wasin");

        teacher1.setPerson(person1);
        person1.setTeacher(teacher1);

        person2 = new Person();
        person2.setId(2L);
        person2.setUserName("person2");
        person2.setEmail("person2@mail.foo");
        person2.setBirthDate(LocalDate.of(1992, 12, 12));
        person2.setGender(Person.Gender.MALE);
        person2.setRole(Role.TEACHER);
        person2.setCreatedAt(LocalDateTime.of(1992, 10, 10, 10, 10));
        person2.setAvatarUrl("uploads/person1_1/avatar2.jpg");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Petya");
        teacher2.setLastName("Pyatkin");

        teacher2.setPerson(person2);
        person2.setTeacher(teacher2);

        teacherList = List.of(teacher1, teacher2);
        expectedTeacherDtoList = teacherList.stream().map(t -> teacherMapper.teacherDto(t)).toList();

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getTeacher() {
        // Given
        TeacherDto expectedDto = expectedTeacherDtoList.get(0);
        when(teacherRepo.findById(1L)).thenReturn(Optional.of(teacher1));
        when(teacherMapper.teacherDto(teacher1)).thenReturn(expectedDto);
        // When
        TeacherDto resultDto = underTest.getTeacher(1L);
        // Then
        verify(teacherRepo).findById(1L);
        assertEquals(expectedDto, resultDto);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenTeacherNotFound() {
        // Given
        Long id = 1L;
        when(teacherRepo.findById(id)).thenReturn(Optional.empty()); // Настройка мока для отсутствия учителя

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getTeacher(id)); // Проверка, что выбрасывается исключение
    }

    @Test
    void getAllTeacher() {
        // When
        when(teacherRepo.findAll()).thenReturn(teacherList);
        when(teacherMapper.allTeacherDto(teacherList)).thenReturn(expectedTeacherDtoList);
        List<TeacherDto> resultTeacherDtoList = underTest.getAllTeacher();
        //Then
        verify(teacherRepo).findAll();
        verify(teacherMapper).allTeacherDto(teacherList);
        assertEquals(expectedTeacherDtoList, resultTeacherDtoList);

    }

    @Test
    void createTeacher() {
        // Given

        // When

        //Then

    }

    @Test
    void deleteTeacher() {
        // Given

        // When

        //Then

    }

    @Test
    void editTeacher() {
        // Given

        // When

        //Then

    }


}