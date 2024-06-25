package com.bodytok.healthdiary.domain;

public interface IBaseImage {
    Long getId();
    String getOriginalFileName();
    String getSavedFileName();
    String getImageUrl();

    void setOriginalFileName(String originalFileName);
    void setSavedFileName(String savedFileName);
    void setImageUrl(String imageUrl);
}
