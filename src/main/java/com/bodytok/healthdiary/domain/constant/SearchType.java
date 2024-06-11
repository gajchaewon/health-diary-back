package com.bodytok.healthdiary.domain.constant;


import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그"),
    DATE("날짜"),
    MONTH("월별");


    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}