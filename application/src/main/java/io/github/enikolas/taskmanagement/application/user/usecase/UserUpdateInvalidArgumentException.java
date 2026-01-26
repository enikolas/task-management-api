package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.ApplicationException;

public class UserUpdateInvalidArgumentException extends ApplicationException {
    public UserUpdateInvalidArgumentException(String message) {
        super(message);
    }

    @Override
    public String code() {
        return "INVALID_ARGUMENT";
    }
}
