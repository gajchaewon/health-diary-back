package com.bodytok.healthdiary.service;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.dto.diaryImage.DiaryImageDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.DiaryImageRepository;
import com.bodytok.healthdiary.util.FileNameConverter;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bodytok.healthdiary.exepction.CustomError.IMAGE_NOT_FOUND;

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

    public DiaryImageDto storeImage(MultipartFile file) {
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

            return DiaryImageDto.from(savedImage);
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
                        .flatMap(imageId -> searchImages(Set.of(imageId)).stream())
                        .collect(Collectors.toSet());

                newImages.forEach(diary::addDiaryImage);

                return diary;
            }
            return diary;
    }

    public void deleteImage(Long imageId) {
        DiaryImage image = diaryImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomBaseException(IMAGE_NOT_FOUND));
        s3Service.removeImage(image);
        diaryImageRepository.deleteById(imageId);
    }

    @Transactional(readOnly = true)
    public Set<DiaryImage> searchImages(Set<Long> imageIds){
        return imageIds.stream()
                .map(diaryImageRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableSet());
    }


}