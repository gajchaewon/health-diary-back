package com.bodytok.healthdiary.dto.diary.response;

import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.Image.ImageResponse;
import com.bodytok.healthdiary.dto.diaryLike.DiaryLikeInfo;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
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
        DiaryLikeInfo likeInfo,
        Set<ImageResponse> images

) {

    public static DiaryResponse of(Long id, String title, String content, Boolean isPublic, Integer totalExTime, LocalDateTime createAt,Long userId, String email, String nickname, Set<HashtagDto> hashtags, DiaryLikeInfo likeInfo, Set<ImageResponse> images){
        return new DiaryResponse(id, title, content, isPublic, totalExTime, createAt,userId, email, nickname, hashtags,likeInfo ,images);

    }

    public static DiaryResponse from(DiaryDto dto) {
        return new DiaryResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.isPublic(),
                dto.totalExTime(),
                dto.createdAt(),
                dto.userAccountDto().id(),
                dto.userAccountDto().email(),
                dto.userAccountDto().nickname(),
                dto.hashtagDtoSet(),
                dto.likeInfo(),
                dto.imageDtoSet().stream()
                        .map(ImageResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}
