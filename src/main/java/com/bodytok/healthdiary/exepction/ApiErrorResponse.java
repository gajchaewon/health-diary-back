package com.bodytok.healthdiary.exepction;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ApiErrorResponse(
        int statusCode,
        String errorCode,
        String message,
        Map<String, String> validation,
        LocalDateTime localDateTime
) {
    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }
}
