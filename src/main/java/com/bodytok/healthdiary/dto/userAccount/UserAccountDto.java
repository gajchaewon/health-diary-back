package com.bodytok.healthdiary.dto.userAccount;

import com.bodytok.healthdiary.domain.ProfileImage;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.Image.ProfileImageDtoImpl;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for {@link UserAccount}
 */
@Builder
public record UserAccountDto(
        Long id,
        String email,
        String nickname,
        String userPassword,
        ProfileImageDtoImpl profileImage,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static UserAccountDto from(UserAccount entity) {
        String nickname = entity.getNickname();
        // 닉네임 null 이면 email 로 반환
        if (nickname == null || nickname.isBlank()) {
            nickname = entity.getEmail();
        }
        return UserAccountDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(nickname)
                .userPassword(entity.getUserPassword())
                .profileImage(ProfileImageDtoImpl.from(
                        entity.getProfileImage() == null ?
                                new ProfileImage() : entity.getProfileImage()
                ))
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }
}