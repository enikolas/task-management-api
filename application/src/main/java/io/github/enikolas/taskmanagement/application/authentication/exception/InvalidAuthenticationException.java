package io.github.enikolas.taskmanagement.application.authentication.exception;

import io.github.enikolas.taskmanagement.application.error.ApplicationException;
import io.github.enikolas.taskmanagement.application.error.ErrorCode;

public class InvalidAuthenticationException extends ApplicationException {
    public InvalidAuthenticationException() {
        super("Authentication invalid: user or credential does not exist");
    }

    @Override
    public ErrorCode code() {
        return ErrorCode.AUTHENTICATION_INVALID;
    }
}
