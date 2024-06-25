package com.bodytok.healthdiary.dto.auth.response;

import com.bodytok.healthdiary.dto.Image.ImageResponse;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        ImageResponse profileImage,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static UserResponse from(UserAccountDto dto){
        String nickname = dto.nickname();
        if (nickname == null || nickname.isBlank()) {
           nickname = dto.nickname();
        }

        return new UserResponse(
                dto.id(),
                dto.email(),
                nickname,
                ImageResponse.from(
                        dto.profileImage()
                ),
                dto.createdAt(),
                dto.modifiedAt()
        );
    }

}
