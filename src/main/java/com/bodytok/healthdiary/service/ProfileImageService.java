package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.ProfileImage;
import com.bodytok.healthdiary.dto.Image.ProfileImageDtoImpl;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.Image.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.bodytok.healthdiary.dto.Image.ImageDtoConverter.profileImageDtoConverter;
import static com.bodytok.healthdiary.exepction.CustomError.IMAGE_NOT_FOUND;

@Transactional
@RequiredArgsConstructor
@Service
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final S3Service s3Service;

    public ProfileImageDtoImpl uploadProfileImage(MultipartFile file) throws Exception {
        return s3Service.uploadImage(file, profileImageDtoConverter, ProfileImage.class, profileImageRepository);
    }

    public void updateImages(UserAccountDto userAccountDto) {

    }

    public void deleteImage(Long imageId) {
        ProfileImage image = profileImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomBaseException(IMAGE_NOT_FOUND));
        s3Service.removeImage(image);
        profileImageRepository.deleteById(imageId);
    }

    @Transactional(readOnly = true)
    public ProfileImageDtoImpl searchImages(Long imageId){
        return profileImageRepository.findById(imageId)
                .map(ProfileImageDtoImpl::from)
                .orElseThrow(() -> new CustomBaseException(IMAGE_NOT_FOUND));
    }

}
