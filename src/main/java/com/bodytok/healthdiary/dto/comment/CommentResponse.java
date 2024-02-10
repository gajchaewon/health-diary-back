package com.bodytok.healthdiary.dto.comment;


import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long userId,
        String email,
        String nickname
) {

    public static CommentResponse of(Long id, String content, LocalDateTime createdAt,Long userId, String email, String nickname) {
        return new CommentResponse(id, content, createdAt, userId, email, nickname);
    }

    public static CommentResponse from(CommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        //닉네임에 대한 유효성 검사
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().email();
        }

        return new CommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().id(),
                dto.userAccountDto().email(),
                nickname
        );
    }
}
