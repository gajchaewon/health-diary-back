package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.Hashtag;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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
        LocalDateTime modifiedAt,
        Set<HashtagDto> hashtagDtoSet

) {

    public static DiaryDto of(UserAccountDto userAccountDto, String title, String content, Boolean isPublic) {
        return new DiaryDto(null, userAccountDto, title, content, 0, isPublic, null, null, Set.of());
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
                entity.getModifiedAt(),
                entity.getDiaryHashtags().stream()
                        .map(PersonalExerciseDiaryHashtag::getHashtag)
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet())
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