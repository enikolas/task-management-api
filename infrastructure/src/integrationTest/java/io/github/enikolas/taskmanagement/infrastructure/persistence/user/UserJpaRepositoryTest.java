package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.util.UUID;

@DataJpaTest
@Testcontainers
@EnableJpaAuditing
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository repository;

    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.1");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("given a new user, when saving, then audit fields are automatically populated")
    void givenNewUser_whenSave_thenAuditFieldsPopulated() {
        var user = UserJpaEntity.builder()
                .fullName("Audit Test")
                .email("audit.test@email.com")
                .passwordHash(UUID.randomUUID().toString())
                .build();

        var savedUser = repository.save(user);

        Assertions.assertThat(savedUser.getId())
                .isNotNull();
        Assertions.assertThat(savedUser.getAudit().getCreatedAt())
                .isNotNull();
        Assertions.assertThat(savedUser.getAudit().getUpdatedAt())
                .isNotNull();
        Assertions.assertThat(savedUser.getAudit().getCreatedAt())
                .isEqualTo(savedUser.getAudit().getUpdatedAt());
    }

    @Test
    @DisplayName("given an existing user, when updating, then updatedAt is updated but createdAt remains unchanged")
    void givenExistingUser_whenUpdate_thenUpdatedAtChanges() {
        var user = repository.save(UserJpaEntity.builder()
                .fullName("Update Test")
                .email("update@email.com")
                .passwordHash(UUID.randomUUID().toString())
                .build());
        var originalCreatedAt = Instant.from(user.getAudit().getCreatedAt());
        var originalUpdatedAt = Instant.from(user.getAudit().getUpdatedAt());

        user.setFullName("Updated Name");
        repository.saveAndFlush(user);

        var updatedUser = repository.findById(user.getId()).get();

        Assertions.assertThat(updatedUser.getAudit().getCreatedAt()).isEqualTo(originalCreatedAt);
        Assertions.assertThat(updatedUser.getAudit().getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("given an active user, when finding by email, then the user is returned")
    void givenActiveUser_whenFindByEmail_thenReturnUser() {
        var email = "john.doe@email.com";
        var user = repository.save(UserJpaEntity.builder()
                .fullName("John Doe")
                .email(email)
                .passwordHash(UUID.randomUUID().toString())
                .deletedAt(null)
                .build());

        var foundOp = repository.findByEmail(email);

        Assertions.assertThat(foundOp)
                .isPresent()
                .get()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("given an inactive user, when finding by email, then no user is returned")
    void givenInactiveUser_whenFindByEmail_thenReturnEmpty() {
        var email = "peter.doe@email.com";
        repository.save(UserJpaEntity.builder()
                .fullName("Peter Doe")
                .email(email)
                .passwordHash(UUID.randomUUID().toString())
                .deletedAt(Instant.now())
                .build());

        var foundOp = repository.findByEmail(email);

        Assertions.assertThat(foundOp).isEmpty();
    }

    @Test
    @DisplayName("given a non-existent user, when finding by email, then no user is returned")
    void givenNonExistentUser_whenFindByEmail_thenReturnEmpty() {
        var email = "mary.doe@email.com";

        var foundOp = repository.findByEmail(email);

        Assertions.assertThat(foundOp).isEmpty();
    }

    @Test
    @DisplayName("given multiple users with the same email, when finding by email, then only the active user is returned")
    void givenActiveAndInactiveUsersWithSameEmail_whenFindByEmail_thenReturnActiveOnly() {
        var email = "duplicate@email.com";

        repository.save(UserJpaEntity.builder()
                .fullName("Old User")
                .email(email)
                .passwordHash(UUID.randomUUID().toString())
                .deletedAt(Instant.now())
                .build());


        var activeUser = repository.save(UserJpaEntity.builder()
                .fullName("New User")
                .email(email)
                .passwordHash(UUID.randomUUID().toString())
                .deletedAt(null)
                .build());

        var foundOp = repository.findByEmail(email);

        Assertions.assertThat(foundOp)
                .isPresent()
                .get()
                .isEqualTo(activeUser);
    }
}
