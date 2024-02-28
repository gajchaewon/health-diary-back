package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link PersonalExerciseDiary}
 */
public record DiaryDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        Integer totalExTime,
        Boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static DiaryDto of(UserAccountDto userAccountDto, String title, String content, Boolean isPublic) {
        return new DiaryDto(null, userAccountDto, title, content, 0, isPublic, null, null);
    }

    public static DiaryDto from(PersonalExerciseDiary entity) {
        return new DiaryDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getTotalExTime(),
                entity.getIsPublic(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public PersonalExerciseDiary toEntity(UserAccount userAccount) {
        return PersonalExerciseDiary.of(
                userAccount,
                title,
                content,
                totalExTime,
                isPublic
        );
    }
}