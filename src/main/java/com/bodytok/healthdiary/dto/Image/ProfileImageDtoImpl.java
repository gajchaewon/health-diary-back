package com.bodytok.healthdiary.dto.Image;

import com.bodytok.healthdiary.domain.ProfileImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImageDtoImpl implements IImageDto<ProfileImage> {
    private Long id;
    private String originalFileName;
    private String savedFileName;
    private String imageUrl;

    public static ProfileImageDtoImpl from(ProfileImage entity) {
        return ProfileImageDtoImpl.builder()
                .id(entity.getId())
                .originalFileName(entity.getOriginalFileName())
                .savedFileName(entity.getSavedFileName())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    @Override
    public ProfileImage toEntity() {
        return ProfileImage.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .imageUrl(imageUrl)
                .build();
    }
}