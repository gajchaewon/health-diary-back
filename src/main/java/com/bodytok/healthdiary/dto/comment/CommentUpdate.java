package com.bodytok.healthdiary.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdate(
        @NotBlank(message = "댓글 내용을 작성해 주세요.")
        @Size(min = 3, max = 500, message = "댓글은 3 ~ 500 자만 가능합니다.")
        String content
) {
}
