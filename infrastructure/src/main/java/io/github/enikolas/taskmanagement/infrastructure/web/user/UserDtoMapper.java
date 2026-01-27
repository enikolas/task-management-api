package io.github.enikolas.taskmanagement.infrastructure.web.user;

import io.github.enikolas.taskmanagement.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public UserResponse toResponseDto(User user) {
        return new UserResponse(
                user.getId().value(),
                user.getFullName(),
                user.getEmail().value()
        );
    }
}
