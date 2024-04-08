package com.bodytok.healthdiary.dto.diaryImage;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;


public record DiaryImageDto(
         Long id,

         String originalFileName,

         String savedFileName,

         String filePath,

         PersonalExerciseDiary personalExerciseDiary
) {

    public static DiaryImageDto of(String originalFileName, String savedFileName, String filePath){
        return new DiaryImageDto(null, originalFileName, savedFileName, filePath, null);
    }

    public static DiaryImageDto from(DiaryImage entity){
        return new DiaryImageDto(
                entity.getId(),
                entity.getOriginalFileName(),
                entity.getSavedFileName(),
                entity.getFilePath(),
                null
        );
    }

    public DiaryImage toEntity(){
        return DiaryImage.of(
                this.originalFileName,
                this.savedFileName,
                this.filePath
        );
    }

}
