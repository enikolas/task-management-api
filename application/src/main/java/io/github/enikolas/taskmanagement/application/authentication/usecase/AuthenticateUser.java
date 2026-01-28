package io.github.enikolas.taskmanagement.application.authentication.usecase;

import io.github.enikolas.taskmanagement.application.authentication.AuthToken;
import io.github.enikolas.taskmanagement.application.authentication.exception.AuthTokenGenerationException;
import io.github.enikolas.taskmanagement.application.authentication.exception.InvalidAuthenticationException;
import io.github.enikolas.taskmanagement.application.authentication.port.AuthTokenRepositoryPort;
import io.github.enikolas.taskmanagement.application.security.port.PasswordCheckerPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;

import java.time.Duration;
import java.time.Instant;

public class AuthenticateUser {
    private final UserRepositoryPort userRepository;
    private final AuthTokenRepositoryPort authTokenRepository;
    private final PasswordCheckerPort passwordChecker;

    private final Duration tokenDuration;

    public AuthenticateUser(UserRepositoryPort userRepository,
                            AuthTokenRepositoryPort authTokenRepository,
                            PasswordCheckerPort passwordChecker,
                            Duration tokenDuration) {
        this.userRepository = userRepository;
        this.authTokenRepository = authTokenRepository;
        this.passwordChecker = passwordChecker;
        this.tokenDuration = tokenDuration;
    }

    public AuthToken authenticateUser(AuthenticateUserInput input) {
        var found = userRepository.findByEmail(input.email());

        if (found.isEmpty()) {
            throw new InvalidAuthenticationException();
        }

        var user = found.get();

        var isCredentialInvalid = !passwordChecker.check(input.plainTextPassword(),
                user.getPasswordHash());

        if (isCredentialInvalid) {
            throw new InvalidAuthenticationException();
        }

        var accessToken = authTokenRepository.generateAccessToken(user.getEmail(),
                getExpiresAt());

        var refreshToken = authTokenRepository.generateRefreshToken(user.getId(), getExpiresAt());

        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
            throw new AuthTokenGenerationException();
        }

        return new AuthToken(accessToken.get(), refreshToken.get());
    }

    private Instant getExpiresAt() {
        return Instant.now().plus(tokenDuration);
    }

}
