package io.github.enikolas.taskmanagement.infrastructure.security;

import io.github.enikolas.taskmanagement.application.security.port.PasswordCheckerPort;
import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordCheckerAdapter implements PasswordCheckerPort {
    @Override
    public boolean check(PlainTextPassword plainTextPassword, PasswordHash passwordHash) {
        return BCrypt.checkpw(plainTextPassword.value(), passwordHash.value());
    }
}
