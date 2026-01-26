package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import io.github.enikolas.taskmanagement.application.user.port.UserRepositoryPort;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserRepositoryJpaAdapter implements UserRepositoryPort {

    private final UserJpaRepository repository;
    private final UserJpaMapper mapper;

    public UserRepositoryJpaAdapter(UserJpaRepository repository, UserJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return repository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmail(email.value())
                .map(mapper::toDomain);
    }

    @Override
    public User create(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        repository.save(entity);

        return mapper.toDomain(entity);
    }

    @Override
    public User update(User user) {
        var entity = repository.getReferenceById(user.getId().value());

        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail().value());
        entity.setPasswordHash(user.getPasswordHash().value());

        repository.save(entity);

        return mapper.toDomain(entity);
    }

    @Override
    public void disable(UserId id) {
        repository.findById(id.value())
                .ifPresent(e -> {
                    e.setDeletedAt(Instant.now());
                    repository.save(e);
                });
    }

}
