package com.bodytok.healthdiary.exepction;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class ApiErrorResponse {

    private int statusCode;
    private String errorCode;
    private String message;
    private Map<String, String> validation;
    private LocalDateTime localDateTime;

    public void addValidation(String field, String errorMessage) {
        if (validation == null) {
            validation = new HashMap<>();
        }
        this.validation.put(field, errorMessage);
    }
}
