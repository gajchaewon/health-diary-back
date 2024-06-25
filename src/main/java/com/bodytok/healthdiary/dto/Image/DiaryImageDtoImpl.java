package com.bodytok.healthdiary.dto.Image;

import com.bodytok.healthdiary.domain.DiaryImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiaryImageDtoImpl implements IImageDto<DiaryImage> {
    private Long id;
    private String originalFileName;
    private String savedFileName;
    private String imageUrl;

    public static DiaryImageDtoImpl from(DiaryImage entity) {
        return DiaryImageDtoImpl.builder()
                .id(entity.getId())
                .originalFileName(entity.getOriginalFileName())
                .savedFileName(entity.getSavedFileName())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    @Override
    public DiaryImage toEntity() {
        return DiaryImage.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .imageUrl(imageUrl)
                .build();
    }

}
