package com.bodytok.healthdiary.dto;

import com.bodytok.healthdiary.domain.UserAccount;

import java.time.LocalDateTime;

/**
 * DTO for {@link UserAccount}
 */
public record UserAccountDto(
        Long id,
        String email,
        String nickname,
        String userPassword,

        Byte[] profileImage,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static UserAccountDto of(String email, String nickname, String userPassword, Byte[] profileImage) {
        return new UserAccountDto(null, email, nickname, userPassword, profileImage, null, null);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserPassword(),
                entity.getProfileImage(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                email,
                nickname,
                userPassword,
                profileImage
        );
    }
}