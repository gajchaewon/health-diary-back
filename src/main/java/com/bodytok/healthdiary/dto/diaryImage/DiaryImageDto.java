package com.bodytok.healthdiary.dto.diaryImage;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import lombok.Builder;


@Builder
public record DiaryImageDto(
         Long id,

         String originalFileName,

         String savedFileName,

         String imageUrl,

         PersonalExerciseDiary personalExerciseDiary
) {

    public static DiaryImageDto of(String originalFileName, String savedFileName, String imageUrl){
        return new DiaryImageDto(null, originalFileName, savedFileName, imageUrl, null);
    }

    public static DiaryImageDto from(DiaryImage entity){
        return new DiaryImageDto(
                entity.getId(),
                entity.getOriginalFileName(),
                entity.getSavedFileName(),
                entity.getImageUrl(),
                null
        );
    }

    public DiaryImage toEntity(){
        return DiaryImage.of(
                this.originalFileName,
                this.savedFileName,
                this.imageUrl
        );
    }

}
