package com.bodytok.healthdiary.service.auth;

import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.TokenType;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.domain.JwtToken;
import com.bodytok.healthdiary.dto.auth.response.*;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import com.bodytok.healthdiary.service.auth.jwt.JwtService;
import com.bodytok.healthdiary.service.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.bodytok.healthdiary.exepction.CustomError.*;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(UserAccountDto dto) {
        userAccountRepository.findByEmail(dto.email()).ifPresent(userAccount -> {
            throw new CustomBaseException(USER_ALREADY_EXISTS);
        });
        CustomUserDetails userDetails = CustomUserDetails.of(
                dto.email(),
                dto.nickname(),
                passwordEncoder.encode(dto.userPassword())
        );
        UserAccount user = CustomUserDetails.toUserEntity(userDetails);
        userAccountRepository.save(user);
        String jwtToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new RegisterResponse(jwtToken, refreshToken);
    }


    public LoginResponse authenticate(UserAccountDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.userPassword()
                )
        );
        UserAccountDto userAccountDto = userAccountRepository.findByEmail(dto.email())
                .map(UserAccountDto::from)
                .orElseThrow(() -> new CustomBaseException(USER_NOT_FOUND));

        CustomUserDetails userDetails = CustomUserDetails.from(userAccountDto);

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        //Access & Refresh Token redis 저장
        JwtToken access = JwtToken.of(accessToken, TokenType.ACCESS);
        JwtToken refresh = JwtToken.of(refreshToken, TokenType.REFRESH);
        jwtService.saveToken(access);
        jwtService.saveToken(refresh);


        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken);
        UserResponse userResponse = UserResponse.from(userAccountDto);
        return new LoginResponse(tokenResponse, userResponse);
    }


    public TokenResponse refreshToken(String accessToken, String refreshToken) {
        try {
            //accessToken 이 redis에 존재한다면 access, refresh 삭제하고 다시 로그인 시키기
            if (accessToken != null && jwtService.getToken(accessToken) != null) {
                jwtService.deleteToken(accessToken);
                jwtService.deleteToken(refreshToken);
                throw new RuntimeException();
            }
            //Refresh redis 조회
            JwtToken refresh = jwtService.getToken(refreshToken);

            //토큰을 통해 유저 정보 추출
            UserDetails userDetails = jwtUtil.getUserDetailsFromToken(refresh.getToken());

            //기존 리프레시 토큰 삭제
            jwtService.deleteToken(refreshToken);

            //새 엑세스 토큰 발급 & redis에 저장
            String newAccessToken = jwtUtil.generateToken(userDetails);
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            jwtService.saveToken(JwtToken.of(newAccessToken, TokenType.ACCESS));
            jwtService.saveToken(JwtToken.of(newRefreshToken, TokenType.REFRESH));

            //엑세스 토큰 반환
            return TokenResponse.of(newAccessToken, newRefreshToken);
        } catch (RuntimeException e) {
            throw new CustomBaseException(REFRESH_LOGOUT);
        }
    }

    public void logout(String accessToken, String refreshToken, CustomUserDetails authUser) {
        UserDetails userDetailsFromToken = jwtUtil.getUserDetailsFromToken(accessToken);
        var userEmail = userDetailsFromToken.getUsername();
        if (!Objects.equals(userEmail, authUser.getUsername())) {
            throw new AccessDeniedException("로그인된 유저 정보와 일치하지 않습니다.");
        }
        jwtService.deleteToken(accessToken);
        jwtService.deleteToken(refreshToken);

        SecurityContextHolder.clearContext();
    }
}
