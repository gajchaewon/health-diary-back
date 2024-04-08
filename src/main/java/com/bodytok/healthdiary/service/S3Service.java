package com.bodytok.healthdiary.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.dto.diaryImage.DiaryImageDto;
import com.bodytok.healthdiary.dto.diaryImage.ImageResponse;
import com.bodytok.healthdiary.repository.DiaryImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class S3Service {

    private final AmazonS3Client s3Client;
    private final DiaryImageRepository diaryImageRepository;
    private final ImageService imageService;
    @Value("${s3.bucket}")
    String bucketName;

    public ImageResponse uploadImage(MultipartFile file) throws IOException {
        //저장될 이미지 이름
        String savedFileName = imageService.convertFileName(file);
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));


        //s3 업로드 메타데이터 set
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        //s3 업로드
        s3Client.putObject(bucketName, savedFileName, file.getInputStream(), metadata);

        String imageUrl = s3Client.getUrl(bucketName, savedFileName).toString();

        DiaryImageDto dto = DiaryImageDto.of(originalFileName, savedFileName, imageUrl);

        DiaryImage savedImage = diaryImageRepository.save(dto.toEntity());

        return ImageResponse.of(savedImage.getId(), imageUrl);

    }


}
