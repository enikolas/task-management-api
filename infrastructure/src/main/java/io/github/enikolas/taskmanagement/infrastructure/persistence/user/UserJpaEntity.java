package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import io.github.enikolas.taskmanagement.infrastructure.persistence.audit.AuditableFields;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uq_users_email_active", columnNames = {"email", "deleted_at"})
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class UserJpaEntity {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private Instant deletedAt;

    @Embedded
    @Builder.Default
    private AuditableFields audit = AuditableFields.builder().build();
}
