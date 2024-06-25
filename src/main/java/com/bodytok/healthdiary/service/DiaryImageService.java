package com.bodytok.healthdiary.service;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.dto.Image.DiaryImageDtoImpl;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.Image.DiaryImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bodytok.healthdiary.dto.Image.ImageDtoConverter.diaryImageDtoConverter;
import static com.bodytok.healthdiary.exepction.CustomError.IMAGE_NOT_FOUND;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class DiaryImageService {

    private final DiaryImageRepository diaryImageRepository;
    private final S3Service s3Service;

    public DiaryImageDtoImpl uploadDiaryImage(MultipartFile file) throws Exception {
        return s3Service.uploadImage(file, diaryImageDtoConverter, DiaryImage.class, diaryImageRepository);
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