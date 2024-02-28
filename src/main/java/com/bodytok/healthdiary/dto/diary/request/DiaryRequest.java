package com.bodytok.healthdiary.dto.diary.request;

import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.diary.DiaryDto;

import java.util.Set;

public record DiaryRequest(
    String title,
    String content,
    Boolean isPublic,
    Set<String> hashtags
) {
    public static DiaryRequest of(String title, String content, Boolean isPublic, Set<String> hashtags) {
        return new DiaryRequest(title,content,isPublic, hashtags);
    }

    public DiaryDto toDto(UserAccountDto userAccountDto) {
        return DiaryDto.of(
                userAccountDto,
                title(),
                content(),
                isPublic()
        );
    }
}
