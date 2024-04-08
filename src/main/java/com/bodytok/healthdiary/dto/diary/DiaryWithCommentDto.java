package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.diaryImage.ImageResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
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
        LocalDateTime modifiedAt,
        Set<HashtagDto> hashtagDtoSet,
        Integer likeCount,

        List<ImageResponse> images
) {

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
                entity.getModifiedAt(),
                entity.getDiaryHashtags().stream()
                        .map(PersonalExerciseDiaryHashtag::getHashtag)
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getLikes().size(),
                entity.getDiaryImages().stream()
                        .map(ImageResponse::from)
                        .collect(Collectors.toList())
        );
    }

}