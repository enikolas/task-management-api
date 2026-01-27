package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.exception.UserDuplicatedException;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class CreateUserTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CreateUser createUser;

    @Test
    @DisplayName("given a duplicated email, when creating a user, then throws an exception")
    void givenDuplicatedEmail_whenCreateUser_thenThrowException() {
        var duplicatedEmail = new Email("already.exists@email.com");

        var user = new User(
                UserId.newId(),
                "Old John Doe",
                duplicatedEmail,
                new PasswordHash("HASHED:old-password")
        );

        var input = new CreateUserInput(
                "John Doe",
                duplicatedEmail,
                new PlainTextPassword("123456789")
        );

        when(userRepository.findByEmail(duplicatedEmail)).thenReturn(Optional.of(user));

        assertThrows(UserDuplicatedException.class, () -> createUser.createUser(input));
    }

    @Test
    @DisplayName("given a valid input, when creating a user, then encode the password and write the user")
    void givenValidInput_whenCreateUser_thenEncodePasswordAndWriteUser() {
        var passwordHash = new PasswordHash("HASHED: 123456789");
        var plainTextPassword = new PlainTextPassword("123456789");

        var input = new CreateUserInput(
                "John Doe",
                new Email("john.doe@email.com"),
                plainTextPassword
        );

        var dummyOutput = new User(
                "Saved John Doe",
                new Email("saved.john.doe@email.com"),
                passwordHash
        );

        when(passwordEncoderPort.encode(plainTextPassword)).thenReturn(passwordHash);
        when(userRepository.create(any())).thenReturn(dummyOutput);

        var output = createUser.createUser(input);

        assertThat(output).isEqualTo(dummyOutput);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).create(captor.capture());

        User saved = captor.getValue();
        assertThat(saved.getFullName()).isEqualTo(input.fullName());
        assertThat(saved.getEmail()).isEqualTo(input.email());
        assertThat(saved.getPasswordHash()).isEqualTo(passwordHash);
    }
}