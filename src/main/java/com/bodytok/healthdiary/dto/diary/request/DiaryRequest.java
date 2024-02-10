package com.bodytok.healthdiary.dto.diary.request;

import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;

public record DiaryRequest(
        String title,
        String content,
        Boolean isPublic,

        String youtubeUrl
) {

    public static DiaryRequest of(String title, String content, Boolean isPublic,  String youtubeUrl){
        return new DiaryRequest( title, content, isPublic, youtubeUrl);

    }

    public PersonalExerciseDiaryDto toDto(UserAccountDto userAccountDto) {

        return PersonalExerciseDiaryDto.of(
                userAccountDto,
                title,
                content,
                isPublic,
                youtubeUrl
        );
    }
}
