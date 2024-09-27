package com.evggenn.school.person;


import com.evggenn.school.role.Role;
import com.evggenn.school.teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username",
            unique = true,
            nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true,
            nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "person")
    private Teacher teacher;

    public enum Gender {
        MALE,
        FEMALE
    }

}
