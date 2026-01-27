package io.github.enikolas.taskmanagement.application.error;

public abstract class ApplicationException extends RuntimeException {
    public abstract ErrorCode code();

    protected ApplicationException(String message) {
        super(message);
    }
}
