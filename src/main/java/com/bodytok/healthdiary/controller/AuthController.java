package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.dto.auth.request.AuthenticationRequest;
import com.bodytok.healthdiary.dto.auth.request.RegisterRequest;
import com.bodytok.healthdiary.dto.auth.response.LoginResponse;
import com.bodytok.healthdiary.dto.auth.response.RefreshTokenResponse;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest request) {
        LoginResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> singUp(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) throws IOException {
        if (refreshToken == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RefreshTokenResponse.of(null,"REFRESH_TOKEN_FAILED"));
        String newRefreshToken = authService.refreshToken(refreshToken);
        if (Objects.equals(newRefreshToken, "REFRESH_TOKEN_FAILED")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RefreshTokenResponse.of(null,"REFRESH_TOKEN_FAILED"));
        }
        return ResponseEntity.ok(RefreshTokenResponse.of(newRefreshToken, "REFRESH_TOKEN_SUCCESS"));
    }
}
