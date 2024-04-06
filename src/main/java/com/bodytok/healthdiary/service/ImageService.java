package com.bodytok.healthdiary.service;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.dto.ImageUploadResponse;
import com.bodytok.healthdiary.repository.DiaryImageRepository;
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

    @Value("${file.uploadDir}")
    private String uploadDir;

    public ImageUploadResponse storeImage(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileNameWithoutExtension = StringUtils.stripFilenameExtension(originalFileName);
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String savedFileName = fileNameWithoutExtension + "_" + UUID.randomUUID() + "." + extension;

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


            return ImageUploadResponse.of(savedImage.getId(), "http://localhost:8080/images/"+savedFileName);
        } catch (IOException ex) {
            throw new RuntimeException("이미지 저장 실패 :" + originalFileName, ex);
        }
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