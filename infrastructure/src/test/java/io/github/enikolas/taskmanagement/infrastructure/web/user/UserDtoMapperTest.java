package io.github.enikolas.taskmanagement.infrastructure.web.user;


import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {

    private final UserDtoMapper mapper = new UserDtoMapper();

    @Test
    @DisplayName("given a domain entity, when mapping to response dto, then map to dto")
    void givenDomainEntity_whenToResponseDto_thenMapDto() {
        var expected = new UserResponse(
                UserId.newId().value(),
                "Katy Smith",
                "katy.smith@email.com"
        );

        var domainEntity = new User(
                new UserId(expected.id()),
                expected.fullName(),
                new Email(expected.email()),
                new PasswordHash("katy-smith-pass-hash")
        );

        var output = mapper.toResponseDto(domainEntity);

        assertThat(output).isEqualTo(expected);
    }

}