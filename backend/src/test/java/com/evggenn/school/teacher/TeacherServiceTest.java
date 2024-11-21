package com.evggenn.school.teacher;

import com.evggenn.school.person.PersonRepo;
import com.evggenn.school.person.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

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

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TeacherService(teacherRepo, personRepo,
                teacherMapper, personService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getTeacher() {
        // Given

        // When

        //Then

    }

    @Test
    void getAllTeacher() {
        // When
        underTest.getAllTeacher();
        //Then
        verify(teacherRepo).findAll();
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

    @Test
    void getTeacherRepo() {
        // Given

        // When

        //Then

    }

    @Test
    void getPersonRepo() {
        // Given

        // When

        //Then

    }

    @Test
    void getTeacherMapper() {
        // Given

        // When

        //Then

    }

    @Test
    void getPersonService() {
        // Given

        // When

        //Then

    }

    @Test
    void testEquals() {
        // Given

        // When

        //Then

    }

    @Test
    void canEqual() {
        // Given

        // When

        //Then

    }

    @Test
    void testHashCode() {
        // Given

        // When

        //Then

    }

    @Test
    void testToString() {
        // Given

        // When

        //Then

    }
}