package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {
    public User toDomain(UserJpaEntity entity) {
        return new User(
                new UserId(entity.getId()),
                entity.getFullName(),
                new Email(entity.getEmail()),
                new PasswordHash(entity.getPasswordHash())
        );
    }

    public UserJpaEntity toJpa(User user) {
        return UserJpaEntity.builder()
                .id(user.getId().value())
                .fullName(user.getFullName())
                .email(user.getEmail().value())
                .passwordHash(user.getPasswordHash().value())
                .build();
    }
}
