package com.bodytok.healthdiary.dto.auth.response;

public record LoginResponse(
        TokenResponse tokenResponse,
        UserResponse userResponse
) {

    public static LoginResponse from(TokenResponse tokenResponse, UserResponse userResponse){
        return new LoginResponse(tokenResponse, userResponse);
    }
}
