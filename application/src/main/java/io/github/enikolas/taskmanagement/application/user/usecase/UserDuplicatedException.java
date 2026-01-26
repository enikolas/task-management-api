package io.github.enikolas.taskmanagement.application.user.usecase;

import io.github.enikolas.taskmanagement.application.ApplicationException;
import io.github.enikolas.taskmanagement.domain.user.Email;

public class UserDuplicatedException extends ApplicationException {
    public UserDuplicatedException(Email email) {
        super("Email already in use: " + email.value());
    }

    @Override
    public String code() {
        return "DUPLICATED";
    }
}
