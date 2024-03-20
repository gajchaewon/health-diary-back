package com.bodytok.healthdiary.exepction;

import java.time.LocalDateTime;

public record CommonApiError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime
) {
}
