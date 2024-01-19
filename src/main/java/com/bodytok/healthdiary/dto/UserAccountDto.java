package com.bodytok.healthdiary.dto;

import com.bodytok.healthdiary.domain.UserAccount;

import java.time.LocalDateTime;

/**
 * DTO for {@link UserAccount}
 */
public record UserAccountDto(
        Long id,
        String email,
        String userPassword,

        String nickname,
        Byte[] profileImage,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static UserAccountDto of(String userPassword, String email, String nickname, Byte[] profileImage) {
        return new UserAccountDto(null, userPassword, email, nickname, profileImage, null, null);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getUserPassword(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getProfileImage(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                id,
                email,
                userPassword,
                nickname,
                profileImage
        );
    }
}