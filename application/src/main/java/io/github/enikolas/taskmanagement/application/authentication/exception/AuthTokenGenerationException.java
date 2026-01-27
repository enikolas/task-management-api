package io.github.enikolas.taskmanagement.application.authentication.exception;

import io.github.enikolas.taskmanagement.application.error.ApplicationException;
import io.github.enikolas.taskmanagement.application.error.ErrorCode;

public class AuthTokenGenerationException extends ApplicationException {
    public AuthTokenGenerationException() {
        super("Failed to generate auth token");
    }

    @Override
    public ErrorCode code() {
        return ErrorCode.AUTH_TOKEN_GENERATION_FAILURE;
    }
}
