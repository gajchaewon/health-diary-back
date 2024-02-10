package com.bodytok.healthdiary.dto.comment;

import com.bodytok.healthdiary.dto.UserAccountDto;

public record CommentRequest(Long diaryId, String content) {


    public static CommentRequest of(Long diaryId, String content) {
        return new CommentRequest(diaryId, content);
    }

    public CommentDto toDto(UserAccountDto userAccountDto) {
        return CommentDto.of(
                diaryId,
                userAccountDto,
                content
        );
    }
}
