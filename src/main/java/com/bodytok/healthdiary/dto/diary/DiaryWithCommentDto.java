package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryWithCommentDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<CommentDto> commentDtoSet,
        String title,
        String content,
        Integer totalExTime,
        Boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static DiaryWithCommentDto of(Long id, UserAccountDto userAccountDto, Set<CommentDto> commentDtoSet, String title, String content, Integer totalExTime, Boolean isPublic, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new DiaryWithCommentDto(id, userAccountDto, commentDtoSet, title, content, totalExTime, isPublic, createdAt, modifiedAt);
    }

    public static DiaryWithCommentDto from(PersonalExerciseDiary entity) {
        return new DiaryWithCommentDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getComments().stream()
                                .map(CommentDto::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getTotalExTime(),
                entity.getIsPublic(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}