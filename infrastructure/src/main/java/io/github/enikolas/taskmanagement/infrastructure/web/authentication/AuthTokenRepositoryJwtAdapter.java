package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.github.enikolas.taskmanagement.application.authentication.port.AuthTokenRepositoryPort;
import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthToken;
import io.github.enikolas.taskmanagement.domain.user.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthTokenRepositoryJwtAdapter implements AuthTokenRepositoryPort {
    private static final String ISSUER = "Task Management API";

    @Value("${task-management-api.security.jwt-token.secret}")
    private String secret;

    @Override
    public Optional<AuthToken> generateToken(Email email,
                                             Instant expiresAt) {
        var algorithm = Algorithm.HMAC256(secret);

        try {
            var token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(email.value())
                    .withExpiresAt(Date.from(expiresAt))
                    .sign(algorithm);

            return Optional.of(new AuthToken(token));
        } catch (JWTCreationException exception) {
            return Optional.empty();
        }
    }
}
