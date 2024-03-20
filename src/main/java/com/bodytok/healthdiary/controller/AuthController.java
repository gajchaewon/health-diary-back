package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.auth.request.AuthenticationRequest;
import com.bodytok.healthdiary.dto.auth.request.RegisterRequest;
import com.bodytok.healthdiary.dto.auth.response.LoginResponse;
import com.bodytok.healthdiary.dto.auth.response.RefreshTokenResponse;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.service.auth.AuthenticationService;
import com.bodytok.healthdiary.service.jwt.JwtService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.authenticate(request);

        var refreshToken = loginResponse.tokenResponse().refreshToken();
        // refreshToken을 쿠키에 담아서 반환
        Cookie refreshTokenCookie = jwtService.createCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> singUp(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken
            ) throws IOException {
        if (refreshToken == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RefreshTokenResponse.of(null,"REFRESH_TOKEN_NULL"));

        String newAccessToken = authService.refreshToken(refreshToken);
        if (Objects.equals(newAccessToken, "REFRESH_TOKEN_FAILED")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RefreshTokenResponse.of(null,"REFRESH_TOKEN_FAILED"));
        }
        return ResponseEntity.ok(RefreshTokenResponse.of(newAccessToken, "REFRESH_TOKEN_SUCCESS"));
    }
}
