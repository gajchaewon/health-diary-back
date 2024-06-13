package com.bodytok.healthdiary.dto.comment;

import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;

import java.time.LocalDateTime;

public record CommentWithDiaryResponse(
        Long id,
        String content,
        Long userId,
        String userEmail,
        DiaryResponse diary,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
