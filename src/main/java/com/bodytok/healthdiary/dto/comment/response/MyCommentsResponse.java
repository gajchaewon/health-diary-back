package com.bodytok.healthdiary.dto.comment.response;

import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record MyCommentsResponse(
        UserResponse userInfo,
        List<CommentWithDiaryResponse> comments
) {
}
