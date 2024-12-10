package com.evggenn.school.person;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepo personRepo;

    public PersonDetailsService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.findPersonByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username " + username + " not found"));
        return new PersonDetails(person);
    }
}
