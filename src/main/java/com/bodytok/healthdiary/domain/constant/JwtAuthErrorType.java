package com.bodytok.healthdiary.domain.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT, without = {})
public enum JwtAuthErrorType {
    WRONG_TYPE_TOKEN(400, "WRONG_TYPE_TOKEN", "잘못된 토큰 타입입니다."),
    UNSUPPORTED_TOKEN(400, "UNSUPPORTED_TOKEN", "지원되지 않는 토큰입니다."),
    EXPIRED_TOKEN(401, "EXPIRED_TOKEN", "만료된 토큰입니다."),
    UNAUTHORIZED(401, "UNAUTHORIZED","인증이 필요합니다."),
    ILLEGAL_ARGUMENT(400, "ILLEGAL_ARGUMENT","토큰 생성 인자가 잘못되었습니다."),
    UNKNOWN_ERROR(500, "UNKNOWN_ERROR", "알 수 없는 오류가 발생했습니다.");

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final int statusCode;
    private final String errorCode;
    private final String message;

    JwtAuthErrorType(int statusCode, String errorCode, String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    public String toJsonString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.info("Json 매핑 에러", e);
            return "";
        }
    }





}