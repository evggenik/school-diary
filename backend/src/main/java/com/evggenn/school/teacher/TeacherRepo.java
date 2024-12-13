package com.evggenn.school.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    @Query("SELECT t FROM Teacher t JOIN t.person p WHERE p.userName = :userName")
    Teacher findByUserName(String userName);

}
