package com.bodytok.healthdiary.dto.auth.request;




public record RegisterRequest(
        String email,
        String password,
        String nickname
) {
}
