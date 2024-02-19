package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.Hashtag;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link PersonalExerciseDiary}
 */
public record PersonalExerciseDiaryDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        Integer totalExTime,
        Boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt

) {

    public static PersonalExerciseDiaryDto of(UserAccountDto userAccountDto, String title, String content, Boolean isPublic) {
        return new PersonalExerciseDiaryDto(null, userAccountDto, title, content, 0, isPublic, null, null);
    }

    public static PersonalExerciseDiaryDto from(PersonalExerciseDiary entity) {
        return new PersonalExerciseDiaryDto(
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