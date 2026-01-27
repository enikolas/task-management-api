package io.github.enikolas.taskmanagement.infrastructure.web.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.enikolas.taskmanagement.application.user.exception.UserDuplicatedException;
import io.github.enikolas.taskmanagement.application.user.exception.UserNotFoundException;
import io.github.enikolas.taskmanagement.application.user.exception.UserUpdateInvalidArgumentException;
import io.github.enikolas.taskmanagement.application.user.usecase.CreateUser;
import io.github.enikolas.taskmanagement.application.user.usecase.UpdateUser;
import io.github.enikolas.taskmanagement.domain.user.Email;
import io.github.enikolas.taskmanagement.domain.user.PasswordHash;
import io.github.enikolas.taskmanagement.domain.user.User;
import io.github.enikolas.taskmanagement.domain.user.UserId;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
@Import({ExceptionMapper.class, UserDtoMapper.class})
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CreateUser createUser;

    @MockitoBean
    private UpdateUser updateUser;

    @Test
    @DisplayName("given no payload, when creating user, then return 400 BAD_REQUEST")
    void givenNoPayload_whenPost_thenReturn400() throws Exception {
        var response = mvc.perform(post("/users"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("given empty payload, when creating user, then return validation errors")
    void givenEmptyPayload_whenPost_thenReturnValidationErrors() throws Exception {
        var response = mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andReturn().getResponse();

        var output = objectMapper.readValue(response.getContentAsString(),
                new TypeReference<List<ValidationErrorResponse>>() {
                });

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(output)
                .extracting(ValidationErrorResponse::field)
                .containsExactlyInAnyOrder("fullName", "email", "password");
    }

    @Test
    @DisplayName("given duplicated email, when creating user, then return 409 CONFLICT")
    void givenDuplicatedEmail_whenPost_thenReturn409() throws Exception {
        var email = "john.doe@email.com";

        when(createUser.createUser(any()))
                .thenThrow(new UserDuplicatedException(new Email(email)));

        var payload = objectMapper.writeValueAsString(
                new UserCreationRequest("John", email, "john-doe-123")
        );

        var response = mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("given valid payload, when creating user, then return 201 CREATED")
    void givenValidPayload_whenPost_thenReturn201() throws Exception {
        var user = new User(
                UserId.newId(),
                "John Doe",
                new Email("john@email.com"),
                new PasswordHash("hashed")
        );

        when(createUser.createUser(any())).thenReturn(user);

        var payload = objectMapper.writeValueAsString(
                new UserCreationRequest("John Doe", "john@email.com", "john-doe-123")
        );

        var response = mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        var output = objectMapper.readValue(
                response.getContentAsString(),
                UserResponse.class
        );

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output.id()).isEqualTo(user.getId().value());
        assertThat(output.fullName()).isEqualTo(user.getFullName());
        assertThat(output.email()).isEqualTo(user.getEmail().value());
    }

    @Test
    @DisplayName("given invalid id, when updating user, then return 400 BAD_REQUEST")
    void givenInvalidId_whenPatch_thenReturn400() throws Exception {
        var response = mvc.perform(
                patch("/users/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("given empty payload, when updating user, then return 400 BAD_REQUEST")
    void givenEmptyPayload_whenPatch_thenReturn400() throws Exception {
        var id = UserId.newId();

        when(updateUser.updateUser(any()))
                .thenThrow(new UserUpdateInvalidArgumentException("No fields provided"));

        var payload = objectMapper.writeValueAsString(
                new UserPatchRequest(null, null, null)
        );

        var response = mvc.perform(
                patch("/users/" + id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("given invalid email, when updating user, then return 400 BAD_REQUEST")
    void givenInvalidEmail_whenPatch_thenReturn400() throws Exception {
        var id = UserId.newId();

        var payload = objectMapper.writeValueAsString(
                new UserPatchRequest("John", "invalid-email", null)
        );

        var response = mvc.perform(
                patch("/users/" + id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
    }


    @Test
    @DisplayName("given non-existent user, when updating, then return 404 NOT_FOUND")
    void givenUserNotFound_whenPatch_thenReturn404() throws Exception {
        var id = UserId.newId();

        when(updateUser.updateUser(any()))
                .thenThrow(new UserNotFoundException(id));

        var payload = objectMapper.writeValueAsString(
                new UserPatchRequest("John", null, null)
        );

        var response = mvc.perform(
                patch("/users/" + id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("given valid payload, when updating user, then return 200 OK")
    void givenValidPayload_whenPatch_thenReturn200() throws Exception {
        var id = UserId.newId();

        var updated = new User(
                id,
                "New Name",
                new Email("new@email.com"),
                new PasswordHash("new-hash")
        );

        when(updateUser.updateUser(any())).thenReturn(updated);

        var payload = objectMapper.writeValueAsString(
                new UserPatchRequest(
                        "New Name",
                        "new@email.com",
                        "new-password-123"
                )
        );

        var response = mvc.perform(
                patch("/users/" + id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        var output = objectMapper.readValue(
                response.getContentAsString(),
                UserResponse.class
        );

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(output.id()).isEqualTo(id.value());
        assertThat(output.fullName()).isEqualTo("New Name");
        assertThat(output.email()).isEqualTo("new@email.com");
    }

    @Test
    @DisplayName("given only name, when updating user, then update only name")
    void givenOnlyName_whenPatch_thenUpdateName() throws Exception {
        var id = UserId.newId();

        var updated = new User(
                id,
                "Only Name Changed",
                new Email("same@email.com"),
                new PasswordHash("same-hash")
        );

        when(updateUser.updateUser(any())).thenReturn(updated);

        var payload = objectMapper.writeValueAsString(
                new UserPatchRequest(
                        "Only Name Changed",
                        null,
                        null
                )
        );

        var response = mvc.perform(
                patch("/users/" + id.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andReturn().getResponse();

        var output = objectMapper.readValue(
                response.getContentAsString(),
                UserResponse.class
        );

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(output.fullName()).isEqualTo("Only Name Changed");
    }
}
