package com.bodytok.healthdiary.exepction;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<?> handleException(
            Exception ex,
            HttpServletRequest request) {
        String message = "Internal Server Error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof EntityNotFoundException) {
            message = ex.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof BadCredentialsException || ex instanceof AccessDeniedException) {
            message = "Authentication failed or user is not authorized";
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof DuplicateKeyException) {
            message = ex.getMessage();
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof HttpMessageNotReadableException) {
            message = "요청이 잘못되었습니다. 유효하지 않은 데이터가 포함되어 있습니다.";
            status = HttpStatus.BAD_REQUEST;
        } else if(ex instanceof IllegalArgumentException){
            message = ex.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof MethodArgumentNotValidException) {
            Map<String, String> errors = new HashMap<>();

            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            var valError = new ValidationError(
                    errors.get("email"),
                    errors.get("password"),
                    errors.get("nickname")
            );
            return new ResponseEntity<>(valError, HttpStatus.BAD_REQUEST);
        }

        CommonApiError apiError = new CommonApiError(
                request.getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }


    @ExceptionHandler(FollowException.class)
    public ResponseEntity<CommonApiError> handleFollowException(
            FollowException ex,
            HttpServletRequest request) {
        CommonApiError apiError = new CommonApiError(
                request.getRequestURI(),
                ex.getMessage(),
                ex.getStatus().value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, ex.getStatus());
    }
}
