package com.bodytok.healthdiary.dto.diary.request;

import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.util.LinkedHashSet;
import java.util.Set;

public record DiaryRequest(
    PersonalExerciseDiaryDto diaryDto,
    Set<HashtagDto> hashtagDtoSet
) {
    public static DiaryRequest of(PersonalExerciseDiaryDto diaryDto, Set<HashtagDto> hashtagDtoSet) {
        return new DiaryRequest(diaryDto, hashtagDtoSet);
    }

    public PersonalExerciseDiaryDto toDto(UserAccountDto userAccountDto) {
        return PersonalExerciseDiaryDto.of(
                userAccountDto,
                diaryDto.title(),
                diaryDto.content(),
                diaryDto.isPublic()
        );
    }
}
