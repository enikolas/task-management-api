package io.github.enikolas.taskmanagement.infrastructure.security;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PasswordHash encode(PlainTextPassword plainTextPassword) {
        return new PasswordHash(passwordEncoder.encode(plainTextPassword.value()));
    }
}
