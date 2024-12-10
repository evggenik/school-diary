package com.evggenn.school.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person, Long> {

    Optional<Person> findPersonByUserName(String username);
}
