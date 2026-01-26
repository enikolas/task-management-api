package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.domain.user.Email;

public record CreateUserInput(
        String fullName,
        Email email,
        PlainTextPassword plainTextPassword
) {
}
