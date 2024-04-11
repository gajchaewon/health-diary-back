package com.bodytok.healthdiary.service;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.dto.diaryImage.ImageResponse;
import com.bodytok.healthdiary.repository.DiaryImageRepository;
import com.bodytok.healthdiary.util.FileNameConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ImageService {

    private final DiaryImageRepository diaryImageRepository;
    private final S3Service s3Service;
    private final FileNameConverter fileNameConverter;


    @Value("${file.uploadDir}")
    private String uploadDir;

    public ImageResponse storeImage(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String savedFileName = fileNameConverter.convertFileName(file);

        log.info("savedFileName : {}", savedFileName);

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(savedFileName);
            Files.copy(file.getInputStream(), filePath);

            DiaryImage diaryImage = DiaryImage.of(originalFileName,savedFileName,filePath.toString());

            DiaryImage savedImage = diaryImageRepository.save(diaryImage);


            return ImageResponse.of(savedImage.getId(), "http://localhost:8080/images/"+savedFileName);
        } catch (IOException ex) {
            throw new RuntimeException("이미지 저장 실패 :" + originalFileName, ex);
        }
    }

    public PersonalExerciseDiary updateImages(PersonalExerciseDiary diary, Set<Long> requestImagesIds) {
        if (!requestImagesIds.isEmpty()) {
            //기존 이미지 id 셋
            Set<Long> existImageIds = diary.getDiaryImages().stream()
                    .map(DiaryImage::getId)
                    .collect(Collectors.toUnmodifiableSet());

            //새로운 이미지들
            Set<DiaryImage> newImages = requestImagesIds.stream()
                    .filter(imageId -> !existImageIds.contains(imageId))
                    .flatMap(imageId -> getImages(Set.of(imageId)).stream())
                    .collect(Collectors.toSet());

            newImages.forEach(diary::addDiaryImage);

            return diary;
        }
        return diary;
    }

    public void deleteImage(Long imageId) {
        DiaryImage image = diaryImageRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("이미지 정보가 없습니다. id : "+ imageId));
        s3Service.removeImage(image);
        diaryImageRepository.deleteById(imageId);
    }

    @Transactional(readOnly = true)
    public Set<DiaryImage> getImages(Set<Long> imageIds){
        return imageIds.stream()
                .map(diaryImageRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableSet());
    }


}