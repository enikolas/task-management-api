package io.github.enikolas.taskmanagement.infrastructure.security;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.usecase.PlainTextPassword;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {
    @Override
    public PasswordHash encode(PlainTextPassword plainTextPassword) {
        return new PasswordHash(BCrypt.hashpw(plainTextPassword.value(), BCrypt.gensalt()));
    }
}
