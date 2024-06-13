package com.bodytok.healthdiary.dto.diaryImage;



public record ImageResponse(
        Long imageId,
        String url
) {

    public static ImageResponse of(Long imageId, String url){
        return new ImageResponse(imageId, url);
    }

    public static ImageResponse toLocalFrom(DiaryImageDto dto){
        return of(
                dto.id(),
                "http://localhost:8080/images/"+dto.savedFileName()
        );
    }
    public static ImageResponse from(DiaryImageDto imageDto){
        return of(
                imageDto.id(),
                imageDto.imageUrl()
        );
    }
}
