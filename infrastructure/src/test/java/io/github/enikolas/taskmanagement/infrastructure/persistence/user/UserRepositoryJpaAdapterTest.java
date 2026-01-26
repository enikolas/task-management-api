package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryJpaAdapterTest {

    @Mock
    private UserJpaRepository repository;

    private UserRepositoryJpaAdapter adapter;

    @BeforeEach
    void setup() {
        UserJpaMapper mapper = new UserJpaMapper();
        adapter = new UserRepositoryJpaAdapter(repository, mapper);
    }

    @Test
    @DisplayName("given an existing user id, when finding by id, then return mapped domain user")
    void givenExistingUserId_whenFindById_thenReturnUser() {
        var id = UserId.newId();
        var expected = new User(
                id,
                "John Doe",
                new Email("john.doe@email.com"),
                new PasswordHash("john-password-hash")
        );

        var entity = UserJpaEntity.builder()
                .id(id.value())
                .fullName(expected.getFullName())
                .email(expected.getEmail().value())
                .passwordHash(expected.getPasswordHash().value())
                .build();

        when(repository.findById(id.value())).thenReturn(Optional.of(entity));

        var result = adapter.findById(id);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("given a non-existent user id, when finding by id, then return empty")
    void givenNonExistentUserId_whenFindById_thenReturnEmpty() {
        var id = UserId.newId();
        when(repository.findById(id.value())).thenReturn(Optional.empty());

        var result = adapter.findById(id);

        assertThat(result).isEmpty();
        verify(repository).findById(id.value());
    }

    @Test
    @DisplayName("given an existing email, when finding by email, then return mapped domain user")
    void givenExistingEmail_whenFindByEmail_thenReturnUser() {
        var email = new Email("jane.doe@email.com");

        var expected = new User(
                UserId.newId(),
                "Jane Doe",
                email,
                new PasswordHash("jane-password-hash")
        );

        var entity = UserJpaEntity.builder()
                .id(expected.getId().value())
                .fullName(expected.getFullName())
                .email(email.value())
                .passwordHash(expected.getPasswordHash().value())
                .build();

        when(repository.findByEmail(email.value())).thenReturn(Optional.of(entity));

        var result = adapter.findByEmail(email);

        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("given a new domain user, when creating, then persist correct jpa entity")
    void givenNewUser_whenCreate_thenPersistAndReturnUser() {
        var domainUser = new User(
                UserId.newId(),
                "Alice Parker",
                new Email("alice.parker@email.com"),
                new PasswordHash("alice-parker-hash")
        );

        adapter.create(domainUser);

        ArgumentCaptor<UserJpaEntity> captor = ArgumentCaptor.forClass(UserJpaEntity.class);
        verify(repository).save(captor.capture());

        var saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(domainUser.getId().value());
        assertThat(saved.getFullName()).isEqualTo(domainUser.getFullName());
        assertThat(saved.getEmail()).isEqualTo(domainUser.getEmail().value());
        assertThat(saved.getPasswordHash()).isEqualTo(domainUser.getPasswordHash().value());
    }

    @Test
    @DisplayName("given an existing domain user, when updating, then persist new values")
    void givenExistingUser_whenUpdate_thenPersistAndReturnUser() {
        var id = UserId.newId();
        var existingEntity = UserJpaEntity.builder()
                .id(id.value())
                .fullName("Old Name")
                .email("old@email.com")
                .passwordHash("old-hash")
                .build();

        var updatedDomain = new User(
                id,
                "New Name",
                new Email("new@email.com"),
                new PasswordHash("new-hash")
        );

        when(repository.getReferenceById(id.value())).thenReturn(existingEntity);

        adapter.update(updatedDomain);

        ArgumentCaptor<UserJpaEntity> captor = ArgumentCaptor.forClass(UserJpaEntity.class);
        verify(repository).save(captor.capture());

        var saved = captor.getValue();
        assertThat(saved.getFullName()).isEqualTo(updatedDomain.getFullName());
        assertThat(saved.getEmail()).isEqualTo(updatedDomain.getEmail().value());
        assertThat(saved.getPasswordHash()).isEqualTo(updatedDomain.getPasswordHash().value());
    }

    @Test
    @DisplayName("given an active user id, when disabling, then mark user as deleted")
    void givenActiveUserId_whenDisable_thenSetDeletedAtAndSave() {
        var id = UserId.newId();
        var entity = UserJpaEntity.builder()
                .id(id.value())
                .fullName("Disabled User")
                .email("disabled@email.com")
                .passwordHash("hash")
                .deletedAt(null)
                .build();

        when(repository.findById(id.value())).thenReturn(Optional.of(entity));

        adapter.disable(id);

        ArgumentCaptor<UserJpaEntity> captor = ArgumentCaptor.forClass(UserJpaEntity.class);
        verify(repository).save(captor.capture());

        assertThat(captor.getValue().getDeletedAt())
                .isNotNull()
                .isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("given a non-existent user id, when disabling, then do nothing")
    void givenNonExistentUserId_whenDisable_thenDoNothing() {
        var id = UserId.newId();
        when(repository.findById(id.value())).thenReturn(Optional.empty());

        adapter.disable(id);

        verify(repository, never()).save(any());
    }
}
