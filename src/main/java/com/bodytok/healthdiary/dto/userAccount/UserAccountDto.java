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
        return new UserAccountDto(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserPassword(),
                ProfileImageDtoImpl.from(entity.getProfileImage() == null ?
                        ProfileImage.builder().build() : entity.getProfileImage()
                ),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}