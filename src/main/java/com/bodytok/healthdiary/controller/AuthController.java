package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.dto.auth.request.AuthenticationRequest;
import com.bodytok.healthdiary.dto.auth.request.RegisterRequest;
import com.bodytok.healthdiary.dto.auth.response.LoginResponse;
import com.bodytok.healthdiary.dto.auth.response.RefreshTokenResponse;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.exepction.CommonApiError;
import com.bodytok.healthdiary.exepction.GlobalExceptionHandler;
import com.bodytok.healthdiary.exepction.ValidationError;
import com.bodytok.healthdiary.service.UserAccountService;
import com.bodytok.healthdiary.service.auth.AuthenticationService;
import com.bodytok.healthdiary.service.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationService authService;
    private final UserAccountService userAccountService;
    private final JwtService jwtService;

    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
    })
    @ApiResponse(responseCode = "401", description = "Bad Credentials", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.authenticate(request);

        var refreshToken = loginResponse.tokenResponse().refreshToken();
        // refreshToken을 쿠키에 담아서 반환
        Cookie refreshTokenCookie = jwtService.createCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/sign-up")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))
    })
    @ApiResponse(responseCode = "400", description = "Bad request", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
    })
    public ResponseEntity<RegisterResponse> singUp(@RequestBody @Valid RegisterRequest request) {
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

    @GetMapping("/check-email")
    @Operation(summary = "Check if email is available")
    public ResponseEntity<Boolean> checkEmail(
            @Parameter(name = "email",description = "email 중복 확인")
            @RequestParam(name = "email") String email){
        return ResponseEntity.ok(userAccountService.checkUserByEmail(email));
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "Check if nickname is available")
    public ResponseEntity<Boolean> checkNickname(
            @Parameter(name = "nickname",description = "nickname 중복 확인")
            @RequestParam(name = "nickname") String nickname){
        return ResponseEntity.ok(userAccountService.checkUserByNickname(nickname));
    }

}
