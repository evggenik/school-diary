package com.evggenn.school.auth;

import com.evggenn.school.jwt.JWTUtil;
import com.evggenn.school.person.PersonDetails;
import com.evggenn.school.teacher.Teacher;
import com.evggenn.school.teacher.TeacherMapper;
import com.evggenn.school.teacher.TeacherRepo;
import com.evggenn.school.teacher.dto.TeacherDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TeacherMapper teacherMapper;
    private final JWTUtil jwtUtil;
    private final TeacherRepo teacherRepo;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 TeacherMapper teacherMapper, JWTUtil jwtUtil, TeacherRepo teacherRepo) {
        this.authenticationManager = authenticationManager;

        this.teacherMapper = teacherMapper;
        this.jwtUtil = jwtUtil;
        this.teacherRepo = teacherRepo;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        PersonDetails principal = (PersonDetails) authentication.getPrincipal();

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Teacher teacher = teacherRepo.findByUserName(principal.getUsername());
        TeacherDto teacherDto = teacherMapper.teacherDto(teacher);
        String token = jwtUtil.issueToken(principal.getUsername(), roles);
        return new AuthenticationResponse(token, teacherDto);
    }

}
