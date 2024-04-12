package com.bodytok.healthdiary.dto.diaryLike;


public record LikeResponse(
        Integer likeCount

) {
    public static LikeResponse of(Integer likeCount) {
        return new LikeResponse(likeCount);
    }
}
