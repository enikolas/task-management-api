package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.error.ApplicationException;
import io.github.enikolas.taskmanagement.application.error.ErrorCode;

public class UserUpdateInvalidArgumentException extends ApplicationException {
    public UserUpdateInvalidArgumentException(String message) {
        super(message);
    }

    @Override
    public ErrorCode code() {
        return ErrorCode.INVALID_ARGUMENT;
    }
}
