package com.bodytok.healthdiary.exepction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomError {

    /**
     * User
     */
    USER_NOT_FOUND(404, "USER_001", "유저를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(400, "USER_002", "이미 존재하는 유저입니다."),
    /**
     * Diary
     */
    DIARY_NOT_FOUND(404, "DIARY_001", "다이어리를 찾을 수 없습니다."),
    DIARY_PRIVATE(403, "DIARY_002", "공개된 다이어리가 아닙니다."),
    NICKNAME_SEARCH_UNSUPPORTED(400, "DIARY_003","개인 다이어리의 닉네임 검색은 지원되지 않습니다."),
    DATE_SEARCH_UNSUPPORTED(400, "DIARY_004","커뮤니티 다이어리의 날짜 검색은 지원되지 않습니다."),
    DIARY_NOT_OWNER(403, "DIARY_005", "다이어리 작성자가 아닙니다."),

    /**
     * Comment
     */
    COMMENT_NOT_FOUND(404, "COMMENT_001", "댓글을 찾을 수 없습니다."),
    COMMENT_NOT_OWNER(403,"COMMENT_002", "댓글 작성자가 아닙니다."),

    /**
     * Routine
     */
    ROUTINE_NOT_FOUND(404, "ROUTINE_001", "루틴 정보를 찾을 수 없습니다."),
    ROUTINE_NOT_OWNER(403, "ROUTINE_002", "루틴 작성자가 아닙니다."),

    /**
     * Follow
     */
    FOLLOW_SELF(400, "FOLLOW_001", "자신을 팔로우 할 수 없습니다."),
    FOLLOW_DUPLICATED(400, "FOLLOW_002", "이미 팔로우를 했습니다."),
    FOLLOW_NOT_FOUND(404,"FOLLOW_003","팔로우 관계가 아닙니다."),

    /**
     * Like
     */
    LIKE_NOT_FOUND(400, "LIKE_001", "좋아요 정보를 찾을 수 없습니다."),

    /**
     * Image
     */
    IMAGE_NOT_FOUND(400, "IMAGE_001", "이미지를 찾을 수 없습니다.");


    private final int statusCode;
    private final String errorCode;
    private final String message;

}
