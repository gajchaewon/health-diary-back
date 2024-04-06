package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.dto.ImageUploadResponse;
import com.bodytok.healthdiary.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries/images")
@Tag(name ="Diary Image")
public class ImageController {

    private final ImageService imageService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 저장", description = "이미지 File 을 저장 - MultipartFile")
    @ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 이미지 ID를 반환함")
    public ImageUploadResponse uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        return imageService.storeImage(file);
    }

}
