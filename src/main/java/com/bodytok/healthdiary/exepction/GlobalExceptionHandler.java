package com.bodytok.healthdiary.exepction;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationError handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ValidationError(
                errors.get("email"),
                errors.get("password"),
                errors.get("nickname")
        );
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonApiError> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request) {
        CommonApiError apiError = new CommonApiError(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonApiError> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {
        String message = "Authentication failed or user is not authorized";
        CommonApiError apiError = new CommonApiError(
                request.getRequestURI(),
                message,
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

}
