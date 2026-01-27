package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
