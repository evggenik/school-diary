package com.evggenn.school.person;


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
            nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String role;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "person")
//    @JsonBackReference
    private Teacher teacher;

}
