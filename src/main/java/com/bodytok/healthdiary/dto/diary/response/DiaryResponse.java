package com.bodytok.healthdiary.dto.diary.response;

import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diaryImage.DiaryImageDto;
import com.bodytok.healthdiary.dto.diaryImage.DiaryImageResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<HashtagDto> hashtags,
        List<String> imageUrls

) {

    public static DiaryResponse of(Long id, String title, String content, Boolean isPublic, Integer totalExTime, LocalDateTime createAt,Long userId, String email, String nickname, Set<HashtagDto> hashtags){
        return new DiaryResponse(id, title, content, isPublic, totalExTime, createAt,userId, email, nickname, hashtags, List.of());

    }

    public static DiaryResponse from(DiaryDto dto) {
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
                dto.hashtagDtoSet(),
                dto.imageUrls()
        );
    }
}
