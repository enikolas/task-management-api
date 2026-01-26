package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.User;

public class CreateUser {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUser(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserInput input) {
        var found = userRepository.findByEmail(input.email());

        if (found.isPresent()) {
            throw new UserDuplicatedException(input.email());
        }

        var passwordHash = passwordEncoder.encode(input.plainTextPassword());
        var user = new User(input.fullName(), input.email(), passwordHash);

        return userRepository.create(user);
    }
}
