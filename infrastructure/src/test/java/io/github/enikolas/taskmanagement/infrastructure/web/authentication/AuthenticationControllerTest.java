package io.github.enikolas.taskmanagement.infrastructure.web.authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.enikolas.taskmanagement.application.authentication.AccessToken;
import io.github.enikolas.taskmanagement.application.authentication.AuthToken;
import io.github.enikolas.taskmanagement.application.authentication.RefreshToken;
import io.github.enikolas.taskmanagement.application.authentication.exception.InvalidAuthenticationException;
import io.github.enikolas.taskmanagement.application.authentication.usecase.AuthenticateUser;
import io.github.enikolas.taskmanagement.infrastructure.web.exception.ExceptionMapper;
import io.github.enikolas.taskmanagement.infrastructure.web.exception.ValidationErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AuthenticationController.class)
@Import({ExceptionMapper.class})
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticateUser authenticateUser;

    @Test
    @DisplayName("given no payload, when logging in, then return 400 BAD_REQUEST")
    void givenNoPayload_whenLogin_thenReturn400() throws Exception {
        var response = mvc.perform(post("/auth/login"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("given empty payload, when logging in, then return validation errors")
    void givenEmptyPayload_whenLogin_thenReturnValidationErrors() throws Exception {
        var response = mvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andReturn().getResponse();

        var output = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<ValidationErrorResponse>>() {
                }
        );

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(output)
                .extracting(ValidationErrorResponse::field)
                .containsExactlyInAnyOrder("username", "password");
    }

    @Test
    @DisplayName("given invalid credentials, when logging in, then return 401 UNAUTHORIZED")
    void givenInvalidCredentials_whenLogin_thenReturn401() throws Exception {
        when(authenticateUser.authenticateUser(any()))
                .thenThrow(new InvalidAuthenticationException());

        var payload = objectMapper.writeValueAsString(
                new AuthenticationRequest("john.doe@email.com", "wrong")
        );

        var response = mvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("given valid credentials, when logging in, then return 200 OK with accessToken")
    void givenValidCredentials_whenLogin_thenReturn200() throws Exception {
        var accessToken = new AccessToken("jwt-access-token");
        var refreshToken = new RefreshToken("jwt-refresh-token");

        var token = new AuthToken(accessToken, refreshToken);

        when(authenticateUser.authenticateUser(any()))
                .thenReturn(token);

        var payload = objectMapper.writeValueAsString(
                new AuthenticationRequest("john.doe@email.com", "correct")
        );

        var response = mvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        var output = objectMapper.readValue(
                response.getContentAsString(),
                AuthenticationResponse.class
        );

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(output.accessToken()).isEqualTo(accessToken.value());
        assertThat(output.refreshToken()).isEqualTo(refreshToken.value());
    }
}
