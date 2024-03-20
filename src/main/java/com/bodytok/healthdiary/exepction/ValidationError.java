package com.bodytok.healthdiary.exepction;

public record ValidationError(
        String emailError,
        String passwordError,
        String nicknameError
) {
}
