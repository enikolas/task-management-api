package io.github.enikolas.taskmanagement.application.security.port;

import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;

@FunctionalInterface
public interface PasswordEncoderPort {
    PasswordHash encode(PlainTextPassword plainTextPassword);
}
