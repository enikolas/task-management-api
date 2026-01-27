package io.github.enikolas.taskmanagement.infrastructure.config.authentication;

import io.github.enikolas.taskmanagement.application.authentication.port.AuthTokenRepositoryPort;
import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthenticateUser;
import io.github.enikolas.taskmanagement.application.security.port.PasswordCheckerPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AuthenticationConfig {

    @Value("${task-management-api.security.jwt-token.duration}")
    private Duration tokenDuration;

    @Bean
    AuthenticateUser authenticateUser(UserRepositoryPort userRepository,
                                      AuthTokenRepositoryPort authTokenRepository,
                                      PasswordCheckerPort passwordChecker) {
        return new AuthenticateUser(userRepository, authTokenRepository, passwordChecker, tokenDuration);
    }
}
