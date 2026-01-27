package io.github.enikolas.taskmanagement.application.security.port;

import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;

public interface PasswordCheckerPort {
    boolean check(PlainTextPassword plainTextPassword, PasswordHash passwordHash);
}
