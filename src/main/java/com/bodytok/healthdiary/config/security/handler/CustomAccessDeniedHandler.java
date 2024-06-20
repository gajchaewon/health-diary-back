package com.bodytok.healthdiary.config.security.handler;

import com.bodytok.healthdiary.exepction.ApiErrorResponse;
import com.bodytok.healthdiary.exepction.CustomError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException{
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(HttpServletResponse.SC_FORBIDDEN)
                .message(ex.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();

        // 응답 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 응답 메시지 설정
        objectMapper.writeValue(response.getWriter(),errorResponse);
    }
}