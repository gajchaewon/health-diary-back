package com.bodytok.healthdiary.dto.comment.response;


import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        UserResponse userInfo
        ) {

    public static CommentResponse from(CommentDto dto) {
        return new CommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.modifiedAt(),
                UserResponse.from(dto.userAccountDto())
        );
    }
}
