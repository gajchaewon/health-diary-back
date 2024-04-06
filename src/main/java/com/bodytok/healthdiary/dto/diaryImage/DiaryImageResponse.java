package com.bodytok.healthdiary.dto.diaryImage;

public record DiaryImageResponse(
        Long id,

        String originalFileName,

        String savedFileName

) {

    public static DiaryImageResponse from(DiaryImageDto dto){
        return new DiaryImageResponse(
                dto.id(),
                dto.originalFileName(),
                dto.savedFileName()
        );
    }

}
