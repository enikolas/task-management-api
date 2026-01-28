package io.github.enikolas.taskmanagement.application.authentication;

public record AuthToken(
        AccessToken accessToken,
        RefreshToken refreshToken
) {
}
