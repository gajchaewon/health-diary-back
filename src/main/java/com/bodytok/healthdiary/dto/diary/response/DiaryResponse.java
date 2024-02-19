package com.bodytok.healthdiary.dto.diary.response;

import com.bodytok.healthdiary.domain.Hashtag;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryResponse(
        Long id,
        String title,
        String content,
        Boolean isPublic,
        Integer totalExTime,
        LocalDateTime createAt,
        String email,
        String nickname,
        Set<HashtagDto> hashtagDtoSet

) {

    public static DiaryResponse of(Long id, String title, String content, Boolean isPublic, Integer totalExTime, LocalDateTime createAt, String email, String nickname, Set<HashtagDto> hashtagDtoSet){
        return new DiaryResponse(id, title, content, isPublic, totalExTime, createAt, email, nickname, hashtagDtoSet);

    }

    public static DiaryResponse from(PersonalExerciseDiaryDto dto, Set<HashtagDto> hashtags) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().email();
        }

        return new DiaryResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.isPublic(),
                dto.totalExTime(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                hashtags
        );
    }
}
