package com.bodytok.healthdiary.dto.diary.response;

import com.bodytok.healthdiary.dto.comment.CommentResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryWithCommentResponse(
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
        Set<CommentResponse> comments,
        Integer likeCount,

        List<String> imageUrls
) {

    public static DiaryWithCommentResponse of(Long id, String title, String content, Boolean isPublic, Integer totalExTime, LocalDateTime createAt,Long userId, String email, String nickname, Set<HashtagDto> hashtags, Set<CommentResponse> comments){
        return new DiaryWithCommentResponse(id, title, content, isPublic, totalExTime, createAt, userId, email, nickname, hashtags, comments, null,List.of());
    }

    public static DiaryWithCommentResponse from(DiaryWithCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().email();
        }

        return new DiaryWithCommentResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.isPublic(),
                dto.totalExTime(),
                dto.createdAt(),
                dto.userAccountDto().id(),
                dto.userAccountDto().email(),
                nickname,
                dto.hashtagDtoSet().stream()
                        .collect(Collectors.toUnmodifiableSet()),
                dto.commentDtoSet().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                dto.likeCount(),
                dto.imageUrls()
        );
    }
}
