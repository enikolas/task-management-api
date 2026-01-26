package io.github.enikolas.taskmanagement.application;

public abstract class ApplicationException extends RuntimeException {
    public abstract String code();

    protected ApplicationException(String message) {
        super(message);
    }
}
