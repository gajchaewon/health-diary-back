package com.bodytok.healthdiary.dto.Image;

import com.bodytok.healthdiary.domain.IBaseImage;

public interface IImageDto<T extends IBaseImage> {
    Long getId();
    String getOriginalFileName();
    String getSavedFileName();
    String getImageUrl();
    T toEntity();
}
