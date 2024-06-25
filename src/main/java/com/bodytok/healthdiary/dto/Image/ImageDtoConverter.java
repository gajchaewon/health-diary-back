package com.bodytok.healthdiary.dto.Image;

import com.bodytok.healthdiary.domain.DiaryImage;
import com.bodytok.healthdiary.domain.ProfileImage;
import com.bodytok.healthdiary.dto.Image.DiaryImageDtoImpl;
import com.bodytok.healthdiary.dto.Image.ProfileImageDtoImpl;

import java.util.function.Function;

public class ImageDtoConverter {
    public static final Function<DiaryImage, DiaryImageDtoImpl> diaryImageDtoConverter = DiaryImageDtoImpl::from;
    public static final Function<ProfileImage, ProfileImageDtoImpl> profileImageDtoConverter = ProfileImageDtoImpl::from;
}
