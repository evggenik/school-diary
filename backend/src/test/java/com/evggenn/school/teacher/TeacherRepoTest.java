package com.evggenn.school.teacher;

import com.evggenn.school.AbstractTestcontainers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeacherRepoTest extends AbstractTestcontainers {

    // here there may be possible tests to test teacher jpa repository
    // for now there are no ones)
    @Test
    void testMethod() {

    }
}