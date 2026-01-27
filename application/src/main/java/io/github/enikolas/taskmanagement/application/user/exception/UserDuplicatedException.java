package io.github.enikolas.taskmanagement.application.user.exception;

import io.github.enikolas.taskmanagement.application.error.ApplicationException;
import io.github.enikolas.taskmanagement.application.error.ErrorCode;
import io.github.enikolas.taskmanagement.domain.user.Email;

public class UserDuplicatedException extends ApplicationException {
    public UserDuplicatedException(Email email) {
        super("Email already in use: " + email.value());
    }

    @Override
    public ErrorCode code() {
        return ErrorCode.DUPLICATED;
    }
}
