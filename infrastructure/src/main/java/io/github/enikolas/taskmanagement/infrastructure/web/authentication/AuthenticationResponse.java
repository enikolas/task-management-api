package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import io.github.enikolas.taskmanagement.application.authentication.AuthToken;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
    public AuthenticationResponse(AuthToken authToken) {
        this(authToken.accessToken().value(), authToken.refreshToken().value());
    }
}
