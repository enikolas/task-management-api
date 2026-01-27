package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthToken;
import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthenticateUser;
import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthenticateUserInput;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticateUser authenticateUser;

    public AuthenticationController(AuthenticateUser authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest payload) {
        AuthenticateUserInput input = new AuthenticateUserInput(
                payload.username(),
                payload.password()
        );

        AuthToken authToken = authenticateUser.authenticateUser(input);

        return ResponseEntity.ok(new AuthenticationResponse(authToken.value()));
    }
}
