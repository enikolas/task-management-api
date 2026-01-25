package io.github.enikolas.taskmanagement.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {
    @Test
    @DisplayName("given a valid data, then a user is created with generated identity")
    void givenValidData_thenCreateUserWithGeneratedId() {
        var user = new User(
                "John Doe",
                new Email("john.doe@email.com"),
                new PasswordHash("hashed:123456789")
        );

        assertNotNull(user.getId());
        assertNotNull(user.getId().value());
    }
}
