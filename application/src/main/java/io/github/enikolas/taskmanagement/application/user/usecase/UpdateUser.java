package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.security.port.PasswordEncoderPort;
import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.User;

import java.util.Optional;

public class UpdateUser {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UpdateUser(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateUser(UpdateUserInput input) {
        if (!input.hasAnyUpdate()) {
            throw new UserUpdateInvalidArgumentException("No fields provided");
        }

        Optional<User> userOp = userRepository.findById(input.userId());

        if (userOp.isEmpty()) {
            throw new UserNotFoundException(input.userId());
        }

        var user = userOp.get();

        var shouldUpdateFullName = updateFullName(input, user);
        var shouldUpdateEmail = updateEmail(input, user);
        var shouldUpdatePassword = updatePassword(input, user);

        var noFieldUpdated = !shouldUpdateFullName
                && !shouldUpdateEmail
                && !shouldUpdatePassword;

        if (noFieldUpdated) {
            throw new UserUpdateInvalidArgumentException("No effective change");
        }

        return userRepository.update(user);
    }

    private boolean updatePassword(UpdateUserInput input, User user) {
        var shouldUpdatePassword = input.plainTextPassword() != null
                && !input.plainTextPassword().value().isBlank();
        if (shouldUpdatePassword) {
            user.setPasswordHash(passwordEncoder.encode(input.plainTextPassword()));
        }
        return shouldUpdatePassword;
    }

    private static boolean updateEmail(UpdateUserInput input, User user) {
        var shouldUpdateEmail = input.email() != null
                && !input.email().value().isBlank()
                && !input.email().equals(user.getEmail());
        if (shouldUpdateEmail) {
            user.setEmail(input.email());
        }
        return shouldUpdateEmail;
    }

    private boolean updateFullName(UpdateUserInput input, User user) {
        var shouldUpdateFullName = input.fullName() != null
                && !input.fullName().isBlank()
                && !input.fullName().equals(user.getFullName());
        if (shouldUpdateFullName) {
            user.setFullName(input.fullName());
        }
        return shouldUpdateFullName;
    }
}
