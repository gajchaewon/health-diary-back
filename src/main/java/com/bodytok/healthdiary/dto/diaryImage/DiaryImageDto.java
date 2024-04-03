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

    public static DiaryImageDto of(Long id,String originalFileName, String savedFileName, String filePath, PersonalExerciseDiary personalExerciseDiary){
        return new DiaryImageDto(id, originalFileName,savedFileName,filePath, null);
    }

    public static DiaryImageDto of(String originalFileName, String savedFileName, String filePath){
        return new DiaryImageDto(null, originalFileName,savedFileName,filePath,null);
    }


    public static DiaryImageDto from(DiaryImage entity){
        return of(
                entity.getId(),
                entity.getOriginalFileName(),
                entity.getSavedFileName(),
                entity.getFilePath(),
                null
        );
    }

}
