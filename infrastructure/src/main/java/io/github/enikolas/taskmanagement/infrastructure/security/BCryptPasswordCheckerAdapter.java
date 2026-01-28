package io.github.enikolas.taskmanagement.infrastructure.security;

import io.github.enikolas.taskmanagement.application.security.port.PasswordCheckerPort;
import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordCheckerAdapter implements PasswordCheckerPort {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean check(PlainTextPassword plainTextPassword, PasswordHash passwordHash) {
        return passwordEncoder.matches(plainTextPassword.value(), passwordHash.value());
    }
}
