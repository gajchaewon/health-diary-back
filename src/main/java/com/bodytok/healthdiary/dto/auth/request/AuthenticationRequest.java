package com.bodytok.healthdiary.dto.auth.request;


import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationRequest(
        @Schema(example = "superman@mail.com")
        String email,

        @Schema(example = "superman1234!")
        String password) {
}