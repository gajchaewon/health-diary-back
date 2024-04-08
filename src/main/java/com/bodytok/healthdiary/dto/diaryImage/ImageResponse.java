package com.bodytok.healthdiary.dto.diaryImage;


import com.bodytok.healthdiary.domain.DiaryImage;

public record ImageResponse(
        Long imageId,
        String url
) {

    public static ImageResponse of(Long imageId, String url){
        return new ImageResponse(imageId, url);
    }

    public static ImageResponse from(DiaryImage image){
        return of(
                image.getId(),
                image.getFilePath()
        );
    }
}
