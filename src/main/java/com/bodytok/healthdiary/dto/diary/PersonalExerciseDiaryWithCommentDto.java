package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link PersonalExerciseDiary}
 */
public record PersonalExerciseDiaryWithCommentDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<CommentDto> commentDtoSet,
        String title,
        String content,
        Integer totalExTime,
        Boolean isPublic,
        String youtubeUrl,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static PersonalExerciseDiaryWithCommentDto of(Long id, UserAccountDto userAccountDto, Set<CommentDto> commentDtoSet, String title, String content, Integer totalExTime,  Boolean isPublic, String youtubeUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new PersonalExerciseDiaryWithCommentDto(id, userAccountDto, commentDtoSet, title, content, totalExTime, isPublic, youtubeUrl, createdAt, modifiedAt);
    }

    public static PersonalExerciseDiaryWithCommentDto from(PersonalExerciseDiary entity) {
        return new PersonalExerciseDiaryWithCommentDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getComments().stream()
                                .map(CommentDto::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getTotalExTime(),
                entity.getIsPublic(),
                entity.getYoutubeUrl(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}