package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.UserId;

public record UpdateUserInput(
        UserId userId,
        String fullName,
        Email email,
        PlainTextPassword plainTextPassword
) {
    public boolean hasAnyUpdate() {
        var isFullNamePresented = fullName != null && !fullName.isBlank();
        var isEmailPresented = email != null && !email.value().isBlank();
        var isPlainTextPasswordPresented = plainTextPassword != null && !plainTextPassword.value().isBlank();

        return isFullNamePresented || isEmailPresented || isPlainTextPasswordPresented;
    }
}
