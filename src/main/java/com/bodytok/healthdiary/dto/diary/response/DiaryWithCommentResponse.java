package com.bodytok.healthdiary.dto.diary.response;

import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import com.bodytok.healthdiary.dto.comment.response.CommentResponse;
import com.bodytok.healthdiary.dto.Image.ImageResponse;
import com.bodytok.healthdiary.dto.diaryLike.DiaryLikeInfo;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryWithCommentResponse(
        Long id,
        String title,
        String content,
        Boolean isPublic,
        Integer totalExTime,
        LocalDateTime createdAt,
        UserResponse userInfo,
        Set<HashtagDto> hashtags,
        Set<CommentResponse> comments,

        DiaryLikeInfo likeInfo,

        Set<ImageResponse> images
) {

    public static DiaryWithCommentResponse from(DiaryWithCommentDto dto) {
        return new DiaryWithCommentResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.isPublic(),
                dto.totalExTime(),
                dto.createdAt(),
                UserResponse.from(dto.userAccountDto()),
                dto.hashtagDtoSet().stream()
                        .collect(Collectors.toUnmodifiableSet()),
                dto.commentDtoSet().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                dto.likeInfo(),
                dto.imagesDtoSet().stream()
                        .map(ImageResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}
