package com.bodytok.healthdiary.repository.comment;

import com.bodytok.healthdiary.dto.comment.response.MyCommentsResponse;

public interface CustomCommentRepository {
    MyCommentsResponse findCommentsByUserId(Long userId);
}
