package io.github.enikolas.taskmanagement.application.authentication.usecase;

import io.github.enikolas.taskmanagement.application.authentication.AccessToken;
import io.github.enikolas.taskmanagement.application.authentication.AuthToken;
import io.github.enikolas.taskmanagement.application.authentication.RefreshToken;
import io.github.enikolas.taskmanagement.application.authentication.exception.AuthTokenGenerationException;
import io.github.enikolas.taskmanagement.application.authentication.exception.InvalidAuthenticationException;
import io.github.enikolas.taskmanagement.application.authentication.port.AuthTokenRepositoryPort;
import io.github.enikolas.taskmanagement.application.security.port.PasswordCheckerPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
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

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AuthTokenRepositoryPort authTokenRepository;

    @Mock
    private PasswordCheckerPort passwordChecker;

    private AuthenticateUser authenticateUser;

    private final Duration tokenDuration = Duration.parse("PT2H");

    @BeforeEach
    void setup() {
        authenticateUser = new AuthenticateUser(userRepository,
                authTokenRepository,
                passwordChecker,
                tokenDuration
        );
    }

    @Test
    @DisplayName("given a non-existent user, when authenticating, then throw an exception")
    void givenUserNotFound_whenAuthenticate_thenThrowException() {
        var email = new Email("notfound@email.com");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        var input = new AuthenticateUserInput(
                email,
                new PlainTextPassword("123")
        );

        assertThrows(InvalidAuthenticationException.class,
                () -> authenticateUser.authenticateUser(input));
    }

    @Test
    @DisplayName("given an invalid password, when authenticating, then throw exception")
    void givenInvalidPassword_whenAuthenticate_thenThrowException() {
        var email = new Email("john.doe@email.com");

        var user = new User(
                UserId.newId(),
                "John Doe",
                email,
                new PasswordHash("HASHED: john-doe-password")
        );

        var input = new AuthenticateUserInput(
                email,
                new PlainTextPassword("wrong")
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordChecker.check(input.plainTextPassword(), user.getPasswordHash()))
                .thenReturn(false);

        assertThrows(InvalidAuthenticationException.class,
                () -> authenticateUser.authenticateUser(input));
    }

    @Test
    @DisplayName("given a token generation failure, when authenticating, then throw exception")
    void givenTokenGenerationFailure_whenAuthenticate_thenThrowException() {
        var email = new Email("chris.parker@email.com");

        var user = new User(
                UserId.newId(),
                "Chirs Parker",
                email,
                new PasswordHash("HASHED: chris-parker-password")
        );

        var input = new AuthenticateUserInput(
                email,
                new PlainTextPassword("chris-parker-password")
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordChecker.check(input.plainTextPassword(), user.getPasswordHash()))
                .thenReturn(true);

        when(authTokenRepository.generateAccessToken(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(AuthTokenGenerationException.class,
                () -> authenticateUser.authenticateUser(input));
    }

    @Test
    @DisplayName("given valid credentials, when authenticating, then return auth token")
    void givenValidCredentials_whenAuthenticate_thenReturnAuthToken() {
        var email = new Email("mary.doe@email.com");

        var user = new User(
                UserId.newId(),
                "Mary Doe",
                email,
                new PasswordHash("HASHED: mary-doe-password")
        );

        var input = new AuthenticateUserInput(
                email,
                new PlainTextPassword("mary-doe-password")
        );

        var accessToken = new AccessToken("my-access-token");
        var refreshToken = new RefreshToken("my-refresh-token");
        var token = new AuthToken(accessToken, refreshToken);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordChecker.check(input.plainTextPassword(), user.getPasswordHash()))
                .thenReturn(true);

        when(authTokenRepository.generateAccessToken(any(), any()))
                .thenReturn(Optional.of(accessToken));

        when(authTokenRepository.generateRefreshToken(any(), any()))
                .thenReturn(Optional.of(refreshToken));

        final Instant minExpiresAt = Instant.now().plus(tokenDuration);

        var output = authenticateUser.authenticateUser(input);

        final Instant maxExpiresAt = Instant.now().plus(tokenDuration);

        assertThat(output).isEqualTo(token);

        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(authTokenRepository).generateAccessToken(eq(email), instantCaptor.capture());

        Instant expiresAt = instantCaptor.getValue();
        assertThat(expiresAt)
                .isNotNull()
                .isBetween(minExpiresAt, maxExpiresAt);
    }

}
