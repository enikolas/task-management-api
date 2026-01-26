package io.github.enikolas.taskmanagement.infrastructure.persistence.user;

import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserJpaMapperTest {

    private final UserJpaMapper mapper = new UserJpaMapper();

    @Test
    @DisplayName("given an jpa entity, when mapping to domain, then map to domain entity")
    void givenJpaEntity_whenToDomain_thenMapDomainEntity() {
        var expected = new User(
                UserId.newId(),
                "Christopher Parker",
                new Email("chistopher.parker@email.com"),
                new PasswordHash("chris-parker-123")
        );

        var jpaEntity = UserJpaEntity.builder()
                .id(expected.getId().value())
                .fullName(expected.getFullName())
                .email(expected.getEmail().value())
                .passwordHash(expected.getPasswordHash().value())
                .build();

        var output = mapper.toDomain(jpaEntity);

        assertThat(output).isEqualTo(expected);
    }

    @Test
    @DisplayName("given a domain entity, when mapping to jpa, then map to jpa entity")
    void givenDomainEntity_whenToJpa_thenMapJpaEntity() {
        var expected = UserJpaEntity.builder()
                .id(UserId.newId().value())
                .fullName("Bob Smith")
                .email("bob.smith@email.com")
                .passwordHash("bob-smith-987")
                .build();

        var domainEntity = new User(
                new UserId(expected.getId()),
                expected.getFullName(),
                new Email(expected.getEmail()),
                new PasswordHash(expected.getPasswordHash())
        );

        var output = mapper.toJpa(domainEntity);

        assertThat(output).isEqualTo(expected);
    }

}
