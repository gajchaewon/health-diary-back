package com.bodytok.healthdiary.dto.auth.response;

public record RegisterResponse(
        String accessToken,
        String refreshToken
) {
}
