package com.bodytok.healthdiary.dto.auth.response;

import com.bodytok.healthdiary.dto.UserAccountDto;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        Byte[] profileImage
) {

    public static UserResponse from(UserAccountDto dto){
        return new UserResponse(
                dto.id(),
                dto.email(),
                dto.nickname(),
                dto.profileImage()
        );
    }
}
