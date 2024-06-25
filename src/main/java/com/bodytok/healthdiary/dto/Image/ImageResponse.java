package com.bodytok.healthdiary.dto.Image;


import com.bodytok.healthdiary.domain.IBaseImage;

public record ImageResponse(
        Long imageId,
        String url
) {

    public static ImageResponse of(Long imageId, String url){
        return new ImageResponse(imageId, url);
    }

    public static <T extends IImageDto<? extends IBaseImage>>  ImageResponse from(T imageDto){
        return of(
                imageDto.getId(),
                imageDto.getImageUrl()
        );
    }

}
