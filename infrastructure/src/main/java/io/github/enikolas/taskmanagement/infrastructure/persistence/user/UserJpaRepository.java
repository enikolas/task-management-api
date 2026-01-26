package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    @Query("""
            SELECT u
            FROM User u
            WHERE UPPER(u.email) = UPPER(:email) AND u.deletedAt IS NULL
            """)
    Optional<UserJpaEntity> findByEmail(String email);

}
