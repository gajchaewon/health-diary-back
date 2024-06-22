package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.dto.diaryImage.ImageResponse;
import com.bodytok.healthdiary.service.ImageService;
import com.bodytok.healthdiary.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries/images")
@Tag(name ="Diary Image")
public class ImageController {

    private final ImageService imageService;
    private final S3Service s3Service;


    //로컬에 저장
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 저장", description = "이미지 File 을 저장 - MultipartFile")
    @ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 이미지 ID와 URL 반환함")
    public ResponseEntity<ImageResponse> uploadImageToLocal(
            @RequestParam("file") MultipartFile file
    ) {
        var response = ImageResponse.toLocalFrom(imageService.storeImage(file));
        return ResponseEntity.ok().body(response);
    }



    //s3에 저장
    @PostMapping(value="/s3" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 저장", description = "이미지 File 을 저장 - MultipartFile")
    @ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 이미지 ID와 URL 반환함")
    public ResponseEntity<ImageResponse> upload(
            @RequestParam("file") MultipartFile file) throws IOException
    {
        var response = ImageResponse.from(s3Service.uploadImage(file));
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{imageId}")
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> remove(
            @PathVariable(name = "imageId") Long imageId
    ){
        imageService.deleteImage(imageId);
        return ResponseEntity.ok().build();
    }

}
