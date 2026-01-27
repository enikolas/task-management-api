package io.github.enikolas.taskmanagement.infrastructure.config.user;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.application.user.usecase.CreateUser;
import io.github.enikolas.taskmanagement.application.user.usecase.UpdateUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    CreateUser createUser(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        return new CreateUser(userRepository, passwordEncoder);
    }

    @Bean
    UpdateUser updateUser(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        return new UpdateUser(userRepository, passwordEncoder);
    }
}
