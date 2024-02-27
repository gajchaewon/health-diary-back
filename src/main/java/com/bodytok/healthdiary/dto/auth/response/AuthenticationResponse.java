package com.bodytok.healthdiary.dto.auth.response;


import com.bodytok.healthdiary.dto.JwtToken;

public record AuthenticationResponse(

        String accessToken,
        String refreshToken
) {
}


