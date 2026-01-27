package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.error.ApplicationException;
import io.github.enikolas.taskmanagement.application.error.ErrorCode;
import io.github.enikolas.taskmanagement.domain.user.UserId;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(UserId userId) {
        super("Attempt to update a user that does not exist! UserId: " + userId.value());
    }

    @Override
    public ErrorCode code() {
        return ErrorCode.NOT_FOUND;
    }
}
