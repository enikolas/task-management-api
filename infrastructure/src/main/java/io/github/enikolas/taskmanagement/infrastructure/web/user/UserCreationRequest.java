package io.github.enikolas.taskmanagement.infrastructure.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreationRequest(
        @NotBlank
        String fullName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 12)
        String password
) {
}
