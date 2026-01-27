package io.github.enikolas.taskmanagement.infrastructure.web.user;

import io.github.enikolas.taskmanagement.application.user.usecase.CreateUser;
import io.github.enikolas.taskmanagement.application.user.usecase.CreateUserInput;
import io.github.enikolas.taskmanagement.application.user.usecase.UpdateUser;
import io.github.enikolas.taskmanagement.application.user.usecase.UpdateUserInput;
import io.github.enikolas.taskmanagement.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUser createUser;
    private final UpdateUser updateUser;
    private final UserDtoMapper dtoMapper;

    public UserController(CreateUser createUser, UpdateUser updateUser, UserDtoMapper dtoMapper) {
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid UserCreationRequest payload,
            UriComponentsBuilder uriBuilder) {

        CreateUserInput input = new CreateUserInput(
                payload.fullName(),
                payload.email(),
                payload.password()
        );
        User registered = createUser.createUser(input);

        var uri = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(registered.getId().value())
                .toUri();

        return ResponseEntity.created(uri).body(dtoMapper.toResponseDto(registered));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<UserResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UserPatchRequest payload) {

        UpdateUserInput input = new UpdateUserInput(
                id,
                payload.fullName(),
                payload.email(),
                payload.password()
        );

        User updated = updateUser.updateUser(input);

        return ResponseEntity.ok(dtoMapper.toResponseDto(updated));
    }
}
