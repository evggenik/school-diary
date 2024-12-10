package com.evggenn.school.teacher;

import com.evggenn.school.exception.ResourceNotFoundException;
import com.evggenn.school.person.Person;
import com.evggenn.school.person.PersonRepo;
import com.evggenn.school.person.PersonService;
import com.evggenn.school.role.Role;
import com.evggenn.school.teacher.dto.EditTeacherDto;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private PasswordEncoder passwordEncoder;

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
                teacherMapper, personService, passwordEncoder);

        person1 = new Person();
        person1.setId(1L);
        person1.setUserName("person1");
        person1.setPassword("password");
        person1.setEmail("person1@mail.foo");
        person1.setBirthDate(LocalDate.of(1990, 10, 10));
        person1.setGender(Person.Gender.MALE);
        person1.setRole(Role.STUDENT);
        person1.setCreatedAt(LocalDateTime.of(1990, 10, 10, 10, 10));
//        person1.setAvatarUrl("uploads/person1_1/avatar1.jpg");

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
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getTeacher() {
        // Given
        expectedTeacherDtoList = teacherList.stream().map(t -> teacherMapper.teacherDto(t)).toList();
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
        when(teacherRepo.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> underTest.getTeacher(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("teacher with id [%s] not found".formatted(id));
//        assertThrows(ResourceNotFoundException.class, () -> underTest.getTeacher(id)); // Проверка, что выбрасывается исключение
    }

    @Test
    void getAllTeacher() {
        // Given
        expectedTeacherDtoList = teacherList.stream().map(t -> teacherMapper.teacherDto(t)).toList();
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
    void createTeacher() throws IOException {
        // Given
        NewTeacherDto newTeacherDto1 = new NewTeacherDto(
                "Wasya", "Wasin", "person1@mail.foo",
                "password", Person.Gender.MALE, Role.STUDENT,
                "person1", LocalDate.of(1990, 10, 10),
                null
        );

        String passwordHash = "d3g9./l02kjfl;";


        when(teacherMapper.toPerson(newTeacherDto1)).thenReturn(person1);
        when(passwordEncoder.encode("password")).thenReturn(passwordHash);
        when(personRepo.save(person1)).thenReturn(person1);
        when(teacherMapper.toTeacher(newTeacherDto1, person1)).thenReturn(teacher1);
        when(teacherRepo.save(teacher1)).thenReturn(teacher1);
        when(teacherMapper.teacherDto(teacher1)).thenReturn(new TeacherDto(
                1L, 1L,
                "Wasya", "Wasin", "person1","person1@mail.foo",
                LocalDate.of(1990, 10, 10), Person.Gender.MALE, Role.STUDENT,
                LocalDateTime.of(1990, 10, 10, 10, 10), null
                )
        );

        // When
        TeacherDto result = underTest.createTeacher(newTeacherDto1, null);

        // Then
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(
                Person.class
        );

        verify(personRepo).save(personArgumentCaptor.capture());
        Person capturedPerson = personArgumentCaptor.getValue();
        assertThat(capturedPerson.getPassword()).isEqualTo(passwordHash);

        assertNotNull(result);
        verify(teacherMapper).toPerson(newTeacherDto1);
        verify(personRepo).save(person1);
        verify(teacherMapper).toTeacher(newTeacherDto1, person1);
        verify(teacherRepo).save(teacher1);
        verify(teacherMapper).teacherDto(teacher1);
    }

    @Test
    void deleteTeacher() {
        // Given
        Long teacherId = 1L;
        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);

        Teacher teacher = new Teacher();
        teacher.setPerson(person);

        when(teacherRepo.findById(teacherId)).thenReturn(Optional.of(teacher));
        // When
        underTest.deleteTeacher(teacherId);
        // Then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(personService).deleteAvatar(captor.capture());
        assertThat(captor.getValue()).isEqualTo(personId);
        verify(teacherRepo).deleteById(teacherId);
    }

    @Test
    void willThrowWhenIdNotFoundWhileCreatingTeacher() {
        // Given
        Long id = 1L;
        when(teacherRepo.findById(id)).thenReturn(Optional.empty());
        // When
        assertThatThrownBy(() -> underTest.deleteTeacher(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Teacher not found with id: " + id);
        // Then
        verify(teacherRepo, never()).deleteById(any());
    }

    @Test
    void editTeacher() throws IOException {
        // Given
        long teacherId = 1;
        EditTeacherDto editTeacherDto = new EditTeacherDto(
                1L,
                "Wasyatka",
                "wasya@mail.foo",
                LocalDate.of(2024, 12, 31),
                Role.PARENT,
                null
        );
        TeacherDto expectedTeacherDto = new TeacherDto(
                1L,
                1L,
                "Wasyatka",
                "Wasin",
                "person1",
                "wasya@mail.foo",
                LocalDate.of(2024, 12, 31),
                Person.Gender.MALE,
                Role.PARENT,
                LocalDateTime.of(1990, 10, 10, 10, 10),
                null
        );
        when(teacherRepo.findById(teacherId)).thenReturn(Optional.of(teacher1));
        when(teacherMapper.toEditPerson(teacher1.getPerson(), editTeacherDto)).thenReturn(person1);
        when(teacherMapper.teacherDto(teacher1)).thenReturn(expectedTeacherDto);
        // When
        TeacherDto result = underTest.editTeacher(teacherId, editTeacherDto, null);
        //Then
        assertThat(result).isEqualTo(expectedTeacherDto);
        verify(teacherRepo).findById(teacherId);
        verify(teacherMapper).toEditPerson(teacher1.getPerson(), editTeacherDto);
        verify(personRepo).save(person1);
        verify(teacherMapper).teacherDto(teacher1);
    }

    @Test
    void willThrowWhenIdNotFoundWhileEditingTeacher() {
        // Given
        long teacherId = 1;
        EditTeacherDto editTeacherDto = new EditTeacherDto(
                1L,
                "Wasya",
                "wasya@mail.foo",
                LocalDate.of(2024, 12, 31),
                Role.PARENT,
                null
        );
        when(teacherRepo.findById(teacherId)).thenReturn(Optional.empty());
        // When
        assertThatThrownBy(() -> underTest.editTeacher(teacherId, editTeacherDto, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("teacher with id [%s] not found".formatted(teacherId));
        // Then
        verify(teacherMapper, never()).toEditPerson(any(), any());
        verify(personRepo, never()).save(any());
        verify(teacherMapper, never()).teacherDto(any());
    }


}