package com.bodytok.healthdiary.dto.comment;

import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.bodytok.healthdiary.domain.Comment}
 */
@Builder
public record CommentDto(
        Long id,
        String content,
        DiaryDto diaryDto,
        UserAccountDto userAccountDto,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {


    public Comment toEntity(PersonalExerciseDiary diary, UserAccount userAccount) {
        return Comment.of(
                userAccount,
                diary,
                content
        );
    }

    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getContent(),
                DiaryDto.from(entity.getPersonalExerciseDiary()),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}