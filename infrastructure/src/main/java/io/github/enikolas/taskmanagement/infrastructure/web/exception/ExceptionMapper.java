package io.github.enikolas.taskmanagement.infrastructure.web.exception;

import io.github.enikolas.taskmanagement.application.error.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException exception) {
        switch (exception.code()) {
            case NOT_FOUND -> {
                return ResponseEntity.notFound().build();
            }
            case DUPLICATED -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
            }
            case INVALID_ARGUMENT -> {
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
            case AUTHENTICATION_INVALID -> {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
            }
            default -> {
                return ResponseEntity.internalServerError().body(exception.getMessage());
            }
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBadRequest(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getFieldErrors();

        return ResponseEntity.badRequest()
                .body(fieldErrors.stream()
                        .map(ValidationErrorResponse::new)
                        .toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadRequest(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
                .body("Required request body is missing");
    }
}
