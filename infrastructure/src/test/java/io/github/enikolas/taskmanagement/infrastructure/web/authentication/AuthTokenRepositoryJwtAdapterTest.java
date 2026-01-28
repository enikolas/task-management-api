package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import io.github.enikolas.taskmanagement.application.authentication.port.AuthTokenRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenRepositoryJwtAdapterTest {
    private final String secret = UUID.randomUUID().toString();

    private final AuthTokenRepositoryPort authTokenRepository = new AuthTokenRepositoryJwtAdapter(secret);

    @Test
    @DisplayName("given an email, when generating an access token, then return an access token")
    void givenEmail_whenGenerateAccessToken_thenReturnAccessToken() {
        var email = new Email("john.doe@email.com");
        var expiresAt = Instant.now().plusSeconds(60L);

        var output = authTokenRepository.generateAccessToken(email, expiresAt);

        assertThat(output).isPresent();
    }

    @Test
    @DisplayName("given a user id, when generating a refresh token, then return a refresh token")
    void givenUserId_whenGenerateRefreshToken_thenReturnRefreshToken() {
        var userId = UserId.newId();
        var expiresAt = Instant.now().plusSeconds(60L);

        var output = authTokenRepository.generateRefreshToken(userId, expiresAt);

        assertThat(output).isPresent();
    }

    @Test
    @DisplayName("given a valid access token, when verifying, then return an email")
    void givenValidAccessToken_whenVerify_thenReturnEmail() {
        var email = new Email("peter.doe@email.com");
        var expiresAt = Instant.now().plusSeconds(60L);
        var accessToken = authTokenRepository.generateAccessToken(email, expiresAt);

        var output = authTokenRepository.verify(accessToken.get());

        assertThat(output).isPresent()
                .get()
                .isEqualTo(email);
    }

    @Test
    @DisplayName("given a valid refresh token, when verifying, then return a userId")
    void givenValidRefreshToken_whenVerify_thenReturnUserId() {
        var userId = UserId.newId();
        var expiresAt = Instant.now().plusSeconds(60L);
        var refreshToken = authTokenRepository.generateRefreshToken(userId, expiresAt);

        var output = authTokenRepository.verify(refreshToken.get());

        assertThat(output).isPresent()
                .get()
                .isEqualTo(userId);
    }
}
