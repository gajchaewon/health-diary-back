package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.Image.DiaryImageDtoImpl;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.dto.diaryLike.DiaryLikeInfo;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link PersonalExerciseDiary}
 */
@Builder
public record DiaryDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        Integer totalExTime,
        Boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Set<HashtagDto> hashtagDtoSet,

        DiaryLikeInfo likeInfo,
        Set<DiaryImageDtoImpl> imageDtoSet

) {

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
                        .collect(Collectors.toUnmodifiableSet()),
                DiaryLikeInfo.from(entity),
                entity.getDiaryImages().stream()
                        .map(DiaryImageDtoImpl::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
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