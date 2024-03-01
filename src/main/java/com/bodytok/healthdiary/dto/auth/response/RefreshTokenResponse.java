package com.bodytok.healthdiary.dto.auth.response;

public record RefreshTokenResponse(
        String accessToken,
        String message
) {
    public static RefreshTokenResponse of(String accessToken, String message){
        return new RefreshTokenResponse(accessToken, message);
    }
}

