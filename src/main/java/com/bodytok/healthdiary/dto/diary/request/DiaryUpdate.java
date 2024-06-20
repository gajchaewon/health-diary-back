package com.bodytok.healthdiary.dto.diary.request;

import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record DiaryUpdate(
        @Schema(description = "제목", example = "멋진 내 다이어리")
        String title,
        @Schema(description = "내용", example = "컨텐츠 내용 - 오늘은 맛있는 커피를 마셨다.")
        String content,
        @Schema(description = "커뮤니티 공개 여부", example = "true")
        Boolean isPublic,
        @Schema(description = "해시태그", nullable = true, examples = {"coffee", "good morning", "drink"})
        Set<String> hashtags,

        @Schema(description = "이미지 아이디", nullable = true)
        Set<Long> imageIds

) {

    public DiaryDto toDtoFromUpdate(Long diaryId, UserAccountDto userAccountDto) {
        return DiaryDto.builder()
                .id(diaryId)
                .userAccountDto(userAccountDto)
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .hashtagDtoSet(hashtags != null ?
                        hashtags.stream().map(HashtagDto::of).collect(Collectors.toSet()) :
                        Collections.emptySet())
                .build();
    }
}
