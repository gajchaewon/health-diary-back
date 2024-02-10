package com.bodytok.healthdiary.dto.comment;

import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.bodytok.healthdiary.domain.Comment}
 */
public record CommentDto(
        Long id,
        Long personalExerciseDiaryId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static CommentDto of(Long personalExerciseDiaryId, UserAccountDto userAccountDto, String content) {
        return new CommentDto(null, personalExerciseDiaryId, userAccountDto, content, null, null);
    }

    public static CommentDto of(Long id, Long personalExerciseDiaryId, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new CommentDto(id, personalExerciseDiaryId, userAccountDto, content, createdAt, modifiedAt);
    }

    //TODO : OSIV 찾아보기

    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getPersonalExerciseDiary().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public Comment toEntity(PersonalExerciseDiary diary, UserAccount userAccount) {
        return Comment.of(
                userAccount,
                diary,
                content
        );
    }

}