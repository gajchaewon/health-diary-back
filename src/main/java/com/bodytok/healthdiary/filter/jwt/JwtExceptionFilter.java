package com.bodytok.healthdiary.filter.jwt;

import com.bodytok.healthdiary.domain.constant.JwtAuthErrorType;
import com.bodytok.healthdiary.exepction.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            //JwtAuthenticationFilter로
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            if(JwtAuthErrorType.ILLEGAL_ARGUMENT.getErrorCode().equals(message)) {
                setResponse(response, JwtAuthErrorType.ILLEGAL_ARGUMENT);
            }
            //잘못된 타입의 토큰인 경우
            else if(JwtAuthErrorType.WRONG_TYPE_TOKEN.getErrorCode().equals(message)) {
                setResponse(response, JwtAuthErrorType.WRONG_TYPE_TOKEN);
            }
            //토큰 만료된 경우
            else if(JwtAuthErrorType.EXPIRED_TOKEN.getErrorCode().equals(message)) {
                setResponse(response, JwtAuthErrorType.EXPIRED_TOKEN);
            }
            //지원되지 않는 토큰인 경우
            else if(JwtAuthErrorType.UNSUPPORTED_TOKEN.getErrorCode().equals(message)) {
                setResponse(response, JwtAuthErrorType.UNSUPPORTED_TOKEN);
            }
            else {
                setResponse(response, JwtAuthErrorType.UNKNOWN_ERROR);
            }
        }
    }

    private void setResponse(HttpServletResponse response, JwtAuthErrorType ex) throws RuntimeException, IOException {

        var errorResponse = ApiErrorResponse.builder()
                .statusCode(ex.getStatusCode())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}