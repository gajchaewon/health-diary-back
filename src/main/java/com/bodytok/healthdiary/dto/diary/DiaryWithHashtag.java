package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.util.Set;

public record DiaryWithHashtag(
        PersonalExerciseDiary diary,
        Set<HashtagDto> hashtags
) {

    public static DiaryWithHashtag of(PersonalExerciseDiary diary, Set<HashtagDto> hashtags) {
        return new DiaryWithHashtag(diary, hashtags);
    }

    public static DiaryResponse toDiaryResponse(DiaryWithHashtag diaryWithHashtag){
        return DiaryResponse.from(DiaryDto.from(diaryWithHashtag.diary),diaryWithHashtag.hashtags());
    }

    public static DiaryWithCommentResponse toDiaryWithCommentResponse(DiaryWithHashtag diaryWithHashtag){
        return DiaryWithCommentResponse.from(
                DiaryWithCommentDto.from(diaryWithHashtag.diary)
                ,diaryWithHashtag.hashtags()
        );
    }
}
