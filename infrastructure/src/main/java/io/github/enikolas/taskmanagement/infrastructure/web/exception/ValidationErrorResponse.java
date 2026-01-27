package io.github.enikolas.taskmanagement.infrastructure.web.exception;

import org.springframework.validation.FieldError;

public record ValidationErrorResponse(
        String field,
        String message
) {
    ValidationErrorResponse(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
