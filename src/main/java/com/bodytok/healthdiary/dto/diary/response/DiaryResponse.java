package com.bodytok.healthdiary.dto.diary.response;

import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;

public record DiaryResponse(
        Long id,
        String title,
        String content,
        Boolean isPublic,
        Integer totalExTime,
        LocalDateTime createAt,
        Long userId,
        String email,
        String nickname,
        Set<HashtagDto> hashtags

) {

    public static DiaryResponse of(Long id, String title, String content, Boolean isPublic, Integer totalExTime, LocalDateTime createAt,Long userId, String email, String nickname, Set<HashtagDto> hashtags){
        return new DiaryResponse(id, title, content, isPublic, totalExTime, createAt,userId, email, nickname, hashtags);

    }

    public static DiaryResponse from(DiaryDto dto, Set<HashtagDto> hashtags) {
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
                dto.userAccountDto().id(),
                dto.userAccountDto().email(),
                nickname,
                hashtags
        );
    }
}
