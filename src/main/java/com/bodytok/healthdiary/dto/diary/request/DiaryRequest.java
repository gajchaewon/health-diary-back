package com.bodytok.healthdiary.dto.diary.request;

import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "다이어리 POST, PUT 요청 바디")
public record DiaryRequest(
        @Schema(description = "제목", example = "멋진 내 다이어리")
        String title,
        @Schema(description = "내용", example = "컨텐츠 내용 - 오늘은 맛있는 커피를 마셨다.")
        String content,
        @Schema(description = "커뮤니티 공개 여부", example = "true")
        Boolean isPublic,
        @Schema(description = "해시태그", nullable = true, examples = {"coffee","good morning","drink"})
        Set<String> hashtags,

        @Schema(description = "이미지 아이디", nullable = true)
        Set<Long> imageIds
) {
    public static DiaryRequest of(String title, String content, Boolean isPublic, Set<String> hashtags, Set<Long> imageIds) {
        return new DiaryRequest(title, content, isPublic, hashtags, imageIds);
    }

    public DiaryDto toDto(UserAccountDto userAccountDto) {
        return DiaryDto.of(
                userAccountDto,
                title(),
                content(),
                isPublic()
        );
    }
}
