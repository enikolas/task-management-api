package io.github.enikolas.taskmanagement.infrastructure.web.user;

import jakarta.validation.constraints.Email;

public record UserPatchRequest(
        String fullName,
        @Email
        String email,
        String password
) {
}
