package com.bodytok.healthdiary.exepction;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<?> handleException(
            Exception ex,
            HttpServletRequest request) {
        String message = "Internal Server Error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof BadCredentialsException){
            message = "Authentication failed or user is not authorized";
            status = HttpStatus.UNAUTHORIZED;
        }else if (ex instanceof AuthorizationDeniedException){
            message = ex.getMessage();
            AuthorizationResult denied = ((AuthorizationDeniedException) ex).getAuthorizationResult();
            log.error("AuthorizationResult - , {}", denied);
            status = HttpStatus.FORBIDDEN;
        }else if (ex instanceof DuplicateKeyException) {
            message = ex.getMessage();
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof HttpMessageNotReadableException) {
            message = "요청이 잘못되었습니다. 유효하지 않은 데이터가 포함되어 있습니다.";
            status = HttpStatus.BAD_REQUEST;
        } else if(ex instanceof IllegalArgumentException){
            message = ex.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof MethodArgumentNotValidException) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .build();
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().forEach(error -> {
                errorResponse.addValidation(error.getField(), error.getDefaultMessage());
            });

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        ApiErrorResponse apiError = ApiErrorResponse.builder()
                .message(message)
                .statusCode(status.value())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<ApiErrorResponse> customBaseException(
            CustomBaseException ex,
            HttpServletRequest request) {
        int statusCode = ex.getError().getStatusCode();
        var response = ApiErrorResponse.builder()
                .message(ex.getError().getMessage())
                .statusCode(statusCode)
                .errorCode(ex.getError().getErrorCode())
                .localDateTime(LocalDateTime.now())
                .validation(ex.getValidation())
                .build();
        return ResponseEntity.status(statusCode).body(response);
    }


    // 업로드 에러
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleFileUploadException(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request){
        ApiErrorResponse apiError = ApiErrorResponse.builder()
                .statusCode(ex.getStatusCode().value())
                .message(ex.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }
}
