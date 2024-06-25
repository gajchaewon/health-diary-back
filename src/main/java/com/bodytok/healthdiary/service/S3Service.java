package com.bodytok.healthdiary.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bodytok.healthdiary.domain.IBaseImage;
import com.bodytok.healthdiary.dto.Image.IImageDto;
import com.bodytok.healthdiary.repository.Image.ImageRepository;
import com.bodytok.healthdiary.util.FileNameConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class S3Service {

    private final AmazonS3Client s3Client;
    private final FileNameConverter fileNameConverter;

    @Value("${s3.bucket}")
    String bucketName;


    public void removeImage(IBaseImage image){
        s3Client.deleteObject(bucketName, image.getSavedFileName());
    }

    public <T extends IBaseImage, D extends IImageDto<T>> D uploadImage(
            MultipartFile file,
            Function<T, D> dtoConverter,
            Class<T> entityType,
            ImageRepository<T> imageRepository) throws Exception{
        //저장될 이미지 이름
        String savedFileName = fileNameConverter.convertFileName(file);
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));


        //s3 업로드 메타데이터 set
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        //s3 업로드
        s3Client.putObject(bucketName, savedFileName, file.getInputStream(), metadata);

        String imageUrl = s3Client.getUrl(bucketName, savedFileName).toString();

        // 클래스의 인스턴스 생성
        T tempImage = entityType.getDeclaredConstructor().newInstance();
        tempImage.setOriginalFileName(originalFileName);
        tempImage.setSavedFileName(savedFileName);
        tempImage.setImageUrl(imageUrl);

        // DTO 변환
        D dto = dtoConverter.apply(tempImage);

        // 엔티티 저장
        T savedImage = imageRepository.save(dto.toEntity());

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        return dtoConverter.apply(savedImage);
    }




}
