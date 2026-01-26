package io.github.enikolas.taskmanagement.application.user.port;

import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    User create(User user);

    User update(User user);

    void disable(UserId id);
}
