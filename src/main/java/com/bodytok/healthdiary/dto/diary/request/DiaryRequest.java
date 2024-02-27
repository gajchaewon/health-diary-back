package com.bodytok.healthdiary.dto.diary.request;

import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.util.LinkedHashSet;
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

    public PersonalExerciseDiaryDto toDto(UserAccountDto userAccountDto) {
        return PersonalExerciseDiaryDto.of(
                userAccountDto,
                title(),
                content(),
                isPublic()
        );
    }
}
