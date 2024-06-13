package com.bodytok.healthdiary.dto.comment.request;


public record CommentCreate(Long diaryId, String content) {

    public static CommentCreate of(Long diaryId, String content) {
        return new CommentCreate(diaryId, content);
    }

}
