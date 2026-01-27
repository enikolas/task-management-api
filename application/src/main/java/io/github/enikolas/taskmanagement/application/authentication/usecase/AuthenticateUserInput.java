package io.github.enikolas.taskmanagement.application.authentication.usecase;

import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.Email;

public record AuthenticateUserInput(
        Email email,
        PlainTextPassword plainTextPassword
) {
    public AuthenticateUserInput(String username, String password) {
        this(new Email(username), new PlainTextPassword(password));
    }
}
