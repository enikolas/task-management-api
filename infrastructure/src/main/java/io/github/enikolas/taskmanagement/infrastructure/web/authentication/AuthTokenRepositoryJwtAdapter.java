package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.github.enikolas.taskmanagement.application.authentication.AccessToken;
import io.github.enikolas.taskmanagement.application.authentication.RefreshToken;
import io.github.enikolas.taskmanagement.application.authentication.port.AuthTokenRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthTokenRepositoryJwtAdapter implements AuthTokenRepositoryPort {
    static final String ISSUER = "Task Management API";

    private final String secret;

    public AuthTokenRepositoryJwtAdapter(@Value("${task-management-api.security.jwt-token.secret}") String secret) {
        this.secret = secret;
    }

    @Override
    public Optional<AccessToken> generateAccessToken(Email email,
                                                     Instant expiresAt) {
        return generateToken(email.value(), expiresAt)
                .map(AccessToken::new);
    }

    @Override
    public Optional<RefreshToken> generateRefreshToken(UserId userId, Instant expiresAt) {
        return generateToken(userId.value().toString(), expiresAt)
                .map(RefreshToken::new);
    }

    @Override
    public Optional<Email> verify(AccessToken accessToken) {
        return verifyToken(accessToken.value())
                .map(Email::new);
    }

    @Override
    public Optional<UserId> verify(RefreshToken refreshToken) {
        return verifyToken(refreshToken.value())
                .map(UUID::fromString)
                .map(UserId::new);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private Optional<String> generateToken(String subject, Instant expiresAt) {
        var algorithm = getAlgorithm();

        try {
            return Optional.of(JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(subject)
                    .withExpiresAt(Date.from(expiresAt))
                    .sign(algorithm));
        } catch (JWTCreationException exception) {
            return Optional.empty();
        }
    }

    private Optional<String> verifyToken(String token) {
        DecodedJWT decodedJWT;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            decodedJWT = verifier.verify(token);

            return Optional.ofNullable(decodedJWT.getSubject());
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
