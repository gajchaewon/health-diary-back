package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.dto.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.UserAccountDto;

import java.time.LocalDateTime;

public record DiaryRequest(
        String title,
        String content,
        Boolean isPublic,

        String youtubeUrl
) {

    public static DiaryRequest of(String title, String content, Boolean isPublic,  String youtubeUrl){
        return new DiaryRequest( title, content, isPublic, youtubeUrl);

    }

    public  PersonalExerciseDiaryDto toDto(UserAccountDto userAccountDto) {

        return PersonalExerciseDiaryDto.of(
                userAccountDto,
                title,
                content,
                isPublic,
                youtubeUrl
        );
    }
}
