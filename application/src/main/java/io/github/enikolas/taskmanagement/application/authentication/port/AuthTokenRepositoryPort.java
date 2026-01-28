package io.github.enikolas.taskmanagement.application.authentication.port;

import io.github.enikolas.taskmanagement.application.authentication.AccessToken;
import io.github.enikolas.taskmanagement.application.authentication.RefreshToken;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.UserId;

import java.time.Instant;
import java.util.Optional;

public interface AuthTokenRepositoryPort {
    Optional<AccessToken> generateAccessToken(Email email, Instant expiresAt);

    Optional<RefreshToken> generateRefreshToken(UserId userId, Instant expiresAt);

    Optional<Email> verify(AccessToken accessToken);

    Optional<UserId> verify(RefreshToken refreshToken);
}
