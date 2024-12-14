package com.evggenn.school.journey;

import com.evggenn.school.auth.AuthenticationRequest;
import com.evggenn.school.auth.AuthenticationResponse;
import com.evggenn.school.jwt.JWTUtil;
import com.evggenn.school.person.Person;
import com.evggenn.school.role.Role;
import com.evggenn.school.teacher.dto.NewTeacherDto;
import com.evggenn.school.teacher.dto.TeacherDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIT {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private WebTestClient webTestClient;

    private static final String AUTHENTICATION_PATH = "/api/v1/auth";
    private static final String TEACHER_PATH = "/api/v1/teachers";
    private final String uniqueSuffix = String.valueOf(System.currentTimeMillis());

    @Test
    void canLogin() {
        // Given
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

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                username,
                password
        );

        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        // send post request
        webTestClient.post()
                .uri(TEACHER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("newTeacherDto", newTeacherDto)
                        .with("avatarFile", avatarFile.getResource()))
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();
        String jwtToken = result.getResponseHeaders().get(AUTHORIZATION).get(0);


        AuthenticationResponse authenticationResponse = result.getResponseBody();
        TeacherDto teacherDto = authenticationResponse.teacherDto();
        assertThat(jwtUtil.isTokenValid(
                jwtToken,
                teacherDto.userName())).isTrue();

        assertThat(teacherDto.email()).isEqualTo(email);
        assertThat(teacherDto.userName()).isEqualTo(username);
        assertThat(teacherDto.firstName()).isEqualTo(firstName);
        assertThat(teacherDto.lastName()).isEqualTo(lastName);
        assertThat(teacherDto.gender()).isEqualTo(gender);
        assertThat(teacherDto.role()).isEqualTo(role);
    }
}
