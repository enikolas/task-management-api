package io.github.enikolas.taskmanagement.application.authentication.port;

import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthToken;
import io.github.enikolas.taskmanagement.domain.user.Email;

import java.time.Instant;
import java.util.Optional;

public interface AuthTokenRepositoryPort {
    Optional<AuthToken> generateToken(Email email, Instant expiresAt);
}
