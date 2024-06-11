package com.bodytok.healthdiary.exepction;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomBaseException extends RuntimeException {
    private CustomError error;
    private final Map<String, String> validation = new HashMap<>();


    public CustomBaseException(CustomError error) {
        this.error = error;
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
