package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.UserId;

import java.util.Optional;
import java.util.UUID;

public record UpdateUserInput(
        UserId userId,
        String fullName,
        Email email,
        PlainTextPassword plainTextPassword
) {
    public UpdateUserInput(UUID id, String fullName, String email, String password) {
        this(new UserId(id),
                fullName,
                Optional.ofNullable(email)
                        .map(Email::new)
                        .orElse(null),
                Optional.ofNullable(password)
                        .map(PlainTextPassword::new)
                        .orElse(null)
        );
    }

    public boolean hasAnyUpdate() {
        var isFullNamePresented = fullName != null && !fullName.isBlank();
        var isEmailPresented = email != null && !email.value().isBlank();
        var isPlainTextPasswordPresented = plainTextPassword != null && !plainTextPassword.value().isBlank();

        return isFullNamePresented || isEmailPresented || isPlainTextPasswordPresented;
    }
}
