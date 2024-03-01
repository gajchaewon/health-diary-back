package com.bodytok.healthdiary.dto.auth.response;

import com.bodytok.healthdiary.dto.UserAccountDto;

public record LoginResponse(
        TokenResponse tokenResponse,
        UserResponse userResponse
) {

    public static LoginResponse from(TokenResponse tokenResponse, UserResponse userResponse){
        return new LoginResponse(tokenResponse, userResponse);
    }
}
