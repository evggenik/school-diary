package com.evggenn.school.journey;

import com.evggenn.school.person.Person;
import com.evggenn.school.role.Role;
import com.evggenn.school.teacher.dto.EditTeacherDto;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TeacherIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String TEACHER_URI = "/api/v1/teachers";
    private final String uniqueSuffix = String.valueOf(System.currentTimeMillis());

    @Test
    void canCreateATeacher() {
        // create registr. request
        String firstName = "Wasya";
        String lastName = "Wasin";
        String email = "person" + uniqueSuffix +"@mail.foo";
        String password = "password";
        Person.Gender gender = Person.Gender.MALE;
        Role role = Role.STUDENT;
        String username = "person" + uniqueSuffix;
        LocalDate birthdate = LocalDate.of(1990, 10, 10);
        String avatarUrl = "uploads/" + username + "/avatar.jpg";

        NewTeacherDto newTeacherDto = new NewTeacherDto(
                firstName,
                lastName,
                email,
                password,
                gender,
                role,
                username,
                birthdate,
                avatarUrl
        );
        // Создаем файл для аватара (пустой файл)
        byte[] avatarBytes = new byte[]{};
        MultipartFile avatarFile = new MockMultipartFile(
                "avatarFile",
                "avatar.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                avatarBytes
        );

        // send post request
        String jwtToken = webTestClient.post()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("newTeacherDto", newTeacherDto)
                        .with("avatarFile", avatarFile.getResource()))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all teachers
        List<TeacherDto> allTeachers = webTestClient.get()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<TeacherDto>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure teacher is present
        TeacherDto expectedTeacherDto = new TeacherDto(
                null,
                null,
                firstName,
                lastName,
                username,
                email,
                birthdate,
                gender,
                role,
                LocalDateTime.of(1990, 10, 10, 10, 10),
                avatarUrl
        );

        assertThat(allTeachers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "personId", "createdAt")
                .contains(expectedTeacherDto);

        TeacherDto createdTeacher = allTeachers.stream()
                        .filter(teacherDto -> teacherDto.email().equals(email))
                                        .findFirst()
                                                .orElseThrow();
        long personId = createdTeacher.personId();
        long id = createdTeacher.id();
        LocalDateTime createdAt = createdTeacher.createdAt();


        TeacherDto actualTeacherDto = new TeacherDto(
                id,
                personId,
                firstName,
                lastName,
                username,
                email,
                birthdate,
                gender,
                role,
                createdAt,
                avatarUrl
        );

        // get teacher by id
        webTestClient.get()
                .uri(TEACHER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<TeacherDto>() {
                })
                .isEqualTo(actualTeacherDto);
    }

    @Test
    void canDeleteTeacher() {
        // create registr. request
        String firstName = "Wasya";
        String lastName = "Wasin";
        String email = "person" + uniqueSuffix +"@mail.foo";
        String password = "password";
        Person.Gender gender = Person.Gender.MALE;
        Role role = Role.STUDENT;
        String username = "person" + uniqueSuffix;
        LocalDate birthdate = LocalDate.of(1990, 10, 10);
        String avatarUrl = "uploads/" + username + "/avatar.jpg";

        NewTeacherDto newTeacherDto = new NewTeacherDto(
                firstName,
                lastName,
                email,
                password,
                gender,
                role,
                username,
                birthdate,
                avatarUrl
        );
        NewTeacherDto newTeacherDto2 = new NewTeacherDto(
                firstName,
                lastName,
                email + "foo",
                password,
                gender,
                role,
                username + "foo",
                birthdate,
                avatarUrl
        );
        // Создаем файл для аватара (пустой файл)
        byte[] avatarBytes = new byte[]{};
        MultipartFile avatarFile = new MockMultipartFile(
                "avatarFile",
                "avatar.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                avatarBytes
        );

        // send post request to create teacher 1
        webTestClient.post()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("newTeacherDto", newTeacherDto)
                        .with("avatarFile", avatarFile.getResource()))
                .exchange()
                .expectStatus()
                .isOk();


        // send post request to create teacher 2
        String jwtToken = webTestClient.post()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("newTeacherDto", newTeacherDto2)
                        .with("avatarFile", avatarFile.getResource()))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all teachers
        List<TeacherDto> allTeachers = webTestClient.get()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<TeacherDto>() {
                })
                .returnResult()
                .getResponseBody();



        TeacherDto createdTeacher = allTeachers.stream()
                .filter(teacherDto -> teacherDto.email().equals(email))
                .findFirst()
                .orElseThrow();
        long personId = createdTeacher.personId();
        long id = createdTeacher.id();
        LocalDateTime createdAt = createdTeacher.createdAt();

        // teacher1 deletes teacher2
        webTestClient.delete()
                .uri(TEACHER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNoContent();

        // gets teacher2 by id
        webTestClient.get()
                .uri(TEACHER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateTeacher() {
        // create registr. request
        String firstName = "Wasya";
        String lastName = "Wasin";
        String email = "person" + uniqueSuffix +"@mail.foo";
        String password = "password";
        Person.Gender gender = Person.Gender.MALE;
        Role role = Role.STUDENT;
        String username = "person" + uniqueSuffix;
        LocalDate birthdate = LocalDate.of(1990, 10, 10);
        String avatarUrl = "uploads/" + username + "/avatar.jpg";

        NewTeacherDto newTeacherDto = new NewTeacherDto(
                firstName,
                lastName,
                email,
                password,
                gender,
                role,
                username,
                birthdate,
                avatarUrl
        );
        // Создаем файл для аватара (пустой файл)
        byte[] avatarBytes = new byte[]{};
        MultipartFile avatarFile = new MockMultipartFile(
                "avatarFile",
                "avatar.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                avatarBytes
        );

        // send post request
        String jwtToken = webTestClient.post()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("newTeacherDto", newTeacherDto)
                        .with("avatarFile", avatarFile.getResource()))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all teachers
        List<TeacherDto> allTeachers = webTestClient.get()
                .uri(TEACHER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<TeacherDto>() {
                })
                .returnResult()
                .getResponseBody();

        // update teacher by id
        TeacherDto createdTeacher = allTeachers.stream()
                .filter(teacherDto -> teacherDto.email().equals(email))
                .findFirst()
                .orElseThrow();
        long personId = createdTeacher.personId();
        long id = createdTeacher.id();
        LocalDateTime createdAt = createdTeacher.createdAt();

        LocalDate newBirthdate = LocalDate.of(1998, 2, 20);

        EditTeacherDto editTeacherDto = new EditTeacherDto(
                personId,
                username,
                email,
                newBirthdate,
                role,
                avatarUrl
        );


        webTestClient.put()
            .uri(TEACHER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("editTeacherDto", editTeacherDto)
                    .with("avatarFile", avatarFile.getResource()))
            .exchange()
            .expectStatus()
            .isOk();


        TeacherDto updatedTeacherDto = webTestClient.get()
            .uri(TEACHER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(TeacherDto.class)
            .returnResult()
            .getResponseBody();

        TeacherDto expectedTeacherDto = new TeacherDto(
            id,
            personId,
            firstName,
            lastName,
            username,
            email,
            newBirthdate,
            gender,
            role,
            createdAt,
            avatarUrl
        );

        assertThat(updatedTeacherDto).isEqualTo(expectedTeacherDto);

    }

}
