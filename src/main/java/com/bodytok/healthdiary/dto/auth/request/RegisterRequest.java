package com.bodytok.healthdiary.dto.auth.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @Schema(example = "superman@mail.com")
        @NotBlank
        @Email(message = "잘못된 이메일 형식입니다.")
        String email,

        @Schema(example = "superman1234!")
        @Pattern(message = "잘못된 비밀번호 형식입니다.",
                regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9$@$!%*#?&]{8,15}$"
        )
        String password,

        @Schema(nullable = true, example = "superman")
        @Pattern(regexp = "[가-힣a-zA-Z0-9]{1,12}$")
        String nickname
) {
}

