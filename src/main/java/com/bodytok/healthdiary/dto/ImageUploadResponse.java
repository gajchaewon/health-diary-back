package com.bodytok.healthdiary.dto;


public record ImageUploadResponse(
        Long imageId,
        String url
) {

    public static ImageUploadResponse of(Long imageId, String url){
        return new ImageUploadResponse(imageId, url);
    }
}
