package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {
    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private UpdateUser updateUser;

    @Test
    @DisplayName("given a non existent user, when updating user, then throws an exception")
    void givenNonExistentUser_whenUpdateUser_thenThrowException() {
        var userId = UserId.newId();

        var input = new UpdateUserInput(
                userId,
                "John Doe Updated",
                null,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateUser.updateUser(input));
    }

    @Test
    @DisplayName("given a null input, when updating user, then throws an exception")
    void givenNullInput_whenUpdateUser_thenThrowException() {
        var userId = UserId.newId();

        var user = new User(userId,
                "John Doe",
                new Email("john.doe@email.com"),
                new PasswordHash("123"));

        var input = new UpdateUserInput(
                userId,
                null,
                null,
                null
        );

        assertThrows(UserUpdateInvalidArgumentException.class, () -> updateUser.updateUser(input));
    }

    @Test
    @DisplayName("given an empty input, when updating user, then throws an exception")
    void givenEmptyInput_whenUpdateUser_thenThrowException() {
        var userId = UserId.newId();

        var input = new UpdateUserInput(
                userId,
                "",
                new Email(""),
                new PlainTextPassword("")
        );

        assertThrows(UserUpdateInvalidArgumentException.class, () -> updateUser.updateUser(input));
    }

    @Test
    @DisplayName("given no change input, when updating user, then throws an exception")
    void givenNoChange_whenUpdateUser_thenThrowException() {
        var userId = UserId.newId();

        var user = new User(userId,
                "Same as Input",
                new Email("same.as.input@email.com"),
                new PasswordHash("some-hashed-password"));

        var input = new UpdateUserInput(
                userId,
                user.getFullName(),
                user.getEmail(),
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(UserUpdateInvalidArgumentException.class, () -> updateUser.updateUser(input));
    }

    @Test
    @DisplayName("given an input with fullName, when updating user, then update fullName")
    void givenFullNameInput_whenUpdateUser_thenUpdateFullName() {
        var userId = UserId.newId();
        var updatedName = "John Married";

        var user = new User(userId,
                "John Doe",
                new Email("john.doe@email.com"),
                new PasswordHash("123"));

        var updated = new User(userId,
                updatedName,
                user.getEmail(),
                user.getPasswordHash());

        var input = new UpdateUserInput(
                userId,
                updatedName,
                null,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.update(any())).thenReturn(updated);

        var output = updateUser.updateUser(input);

        assertThat(output).isEqualTo(updated);

        verify(userRepository).update(updated);
    }

    @Test
    @DisplayName("given an input with email, when updating user, then update email")
    void givenEmailInput_whenUpdateUser_thenUpdateEmail() {
        var userId = UserId.newId();
        var updatedEmail = new Email("peter.married@email.com");

        var user = new User(userId,
                "Peter Doe",
                new Email("john.doe@email.com"),
                new PasswordHash("456"));

        var updated = new User(userId,
                user.getFullName(),
                updatedEmail,
                user.getPasswordHash());

        var input = new UpdateUserInput(
                userId,
                null,
                updatedEmail,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.update(any())).thenReturn(updated);

        var output = updateUser.updateUser(input);

        assertThat(output).isEqualTo(updated);

        verify(userRepository).update(updated);
    }

    @Test
    @DisplayName("given an input with password, when updating user, then encode and update password")
    void givenPasswordInput_whenUpdateUser_thenEncodeAndUpdatePassword() {
        var userId = UserId.newId();
        var newPassword = new PlainTextPassword("new-password");
        var updatedPassword = new PasswordHash("HASH:new-password");

        var user = new User(userId,
                "Bob Doe",
                new Email("bob.doe@email.com"),
                new PasswordHash("old-password"));

        var updated = new User(userId,
                user.getFullName(),
                user.getEmail(),
                updatedPassword);

        var input = new UpdateUserInput(
                userId,
                null,
                null,
                newPassword
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(updatedPassword);
        when(userRepository.update(any())).thenReturn(updated);

        var output = updateUser.updateUser(input);

        assertThat(output).isEqualTo(updated);

        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).update(updated);
    }

    @Test
    @DisplayName("given a full input, when updating user, then update all fields")
    void givenFullInput_whenUpdateUser_thenUpdateAllFields() {
        var userId = UserId.newId();
        var updatedName = "Chris Park";
        var updatedEmail = new Email("chris.park@email.com");
        var newPassword = new PlainTextPassword("new-password");
        var updatedPassword = new PasswordHash("HASH:new-password");

        var user = new User(userId,
                "Chris Doe",
                new Email("chris.doe@email.com"),
                new PasswordHash("old-password"));

        var updated = new User(userId,
                updatedName,
                updatedEmail,
                updatedPassword);

        var input = new UpdateUserInput(
                userId,
                updatedName,
                updatedEmail,
                newPassword
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(updatedPassword);
        when(userRepository.update(any())).thenReturn(updated);

        var output = updateUser.updateUser(input);

        assertThat(output).isEqualTo(updated);

        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).update(updated);
    }

}