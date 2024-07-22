package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.auth.request.UserLogin;
import com.bodytok.healthdiary.dto.auth.request.UserRegister;
import com.bodytok.healthdiary.dto.auth.response.LoginResponse;
import com.bodytok.healthdiary.dto.auth.response.RefreshTokenResponse;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.dto.auth.response.TokenResponse;
import com.bodytok.healthdiary.dto.userAccount.UserAccountMapper;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.service.UserAccountService;
import com.bodytok.healthdiary.service.auth.AuthenticationService;
import com.bodytok.healthdiary.service.auth.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.bodytok.healthdiary.exepction.CustomError.REFRESH_TOKEN_NULL;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {
    private final UserAccountMapper userAccountMapper = UserAccountMapper.INSTANCE;
    private final AuthenticationService authService;
    private final UserAccountService userAccountService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<LoginResponse> login(
            @RequestBody UserLogin request,
            HttpServletResponse response
    ) {
        var toDto = userAccountMapper.toDtoFromLogin(request);

        LoginResponse loginResponse = authService.authenticate(toDto);

        String refreshToken = loginResponse.tokenResponse().refreshToken();
        // refreshToken을 쿠키에 담아서 반환
        Cookie refreshTokenCookie = jwtUtil.createCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public ResponseEntity<RegisterResponse> singUp(@RequestBody @Valid UserRegister request) {
        var toDto = userAccountMapper.toDtoFromRegister(request);
        RegisterResponse response = authService.register(toDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String accessToken = parseTokenFromHeader(request);
        if (accessToken != null) {
            authService.logout(accessToken, refreshToken, userDetails);
            return ResponseEntity.ok().build();
        } else {
            // Authorization 헤더가 없거나 형식이 올바르지 않은 경우 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "리프레시 토큰 발급", description = "쿠키에 있는 리프레시 토큰을 통해 발급")
    public ResponseEntity<TokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        if (refreshToken == null) {
            throw new CustomBaseException(REFRESH_TOKEN_NULL);
        }
        String accessToken = parseTokenFromHeader(request);
        TokenResponse reIssueResponse = authService.refreshToken(accessToken, refreshToken);
        Cookie refreshTokenCookie = jwtUtil.createCookie(reIssueResponse.refreshToken());
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok().body(reIssueResponse);
    }

    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복 확인")
    public ResponseEntity<Boolean> checkEmail(
            @Parameter(name = "email", description = "email 중복 확인")
            @RequestParam(name = "email") String email) {
        return ResponseEntity.ok(userAccountService.checkUserByEmail(email));
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복 확인")
    public ResponseEntity<Boolean> checkNickname(
            @Parameter(name = "nickname", description = "nickname 중복 확인")
            @RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(userAccountService.checkUserByNickname(nickname));
    }

    private String parseTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
