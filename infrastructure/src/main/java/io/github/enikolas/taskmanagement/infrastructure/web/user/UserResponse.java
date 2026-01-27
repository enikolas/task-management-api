package io.github.enikolas.taskmanagement.infrastructure.web.user;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email
) {
}
