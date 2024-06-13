package com.bodytok.healthdiary.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdate(
        @NotBlank(message = "Content cannot be blank")
        @Size(min = 2, max = 500, message = "Content must be between 5 and 500 characters")
        String content
) {
}
