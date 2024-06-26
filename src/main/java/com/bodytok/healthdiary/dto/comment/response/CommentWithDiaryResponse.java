package com.bodytok.healthdiary.dto.comment.response;

import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;

import java.time.LocalDateTime;

public record CommentWithDiaryResponse(
        Long id,
        String content,
        DiaryResponse diary,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static CommentWithDiaryResponse from(CommentDto dto) {
        return new CommentWithDiaryResponse(
                dto.id(),
                dto.content(),
                DiaryResponse.from(dto.diaryDto()),
                dto.createdAt(),
                dto.modifiedAt()
        );
    }

}
