package com.bodytok.healthdiary.dto.diary;

import com.bodytok.healthdiary.dto.PersonalExerciseDiaryDto;

import java.time.LocalDateTime;

public record DiaryResponse(
        Long id,
        String title,
        String content,
        Boolean isPublic,
        Integer totalExTime,
        String youtubeUrl,
        LocalDateTime createAt,

        String email,
        String nickname

) {

    public static DiaryResponse of(Long id, String title, String content, Boolean isPublic, Integer totalExTime, String youtubeUrl, LocalDateTime createAt, String email, String nickname){
        return new DiaryResponse(id, title, content, isPublic, totalExTime, youtubeUrl, createAt, email, nickname);

    }

    public static DiaryResponse from(PersonalExerciseDiaryDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().email();
        }

        return new DiaryResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.isPublic(),
                dto.totalExTime(),
                dto.youtubeUrl(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                dto.userAccountDto().nickname()
        );
    }
}
