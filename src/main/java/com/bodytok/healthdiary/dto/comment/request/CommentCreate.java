package com.bodytok.healthdiary.dto.comment.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreate(
        @NotNull
        Long diaryId,
        @NotBlank(message = "댓글 내용을 작성해 주세요.")
        @Size(min = 3, max = 500, message = "3글자 이상 작성해 주세요.")
        String content
) {

    public static CommentCreate of(Long diaryId, String content) {
        return new CommentCreate(diaryId, content);
    }

}
