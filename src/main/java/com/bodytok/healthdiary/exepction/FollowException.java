package com.bodytok.healthdiary.exepction;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FollowException extends RuntimeException{

    private final HttpStatus status;

    public FollowException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
