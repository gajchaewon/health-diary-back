package com.bodytok.healthdiary.service.auth;

import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.TokenType;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.domain.JwtToken;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.dto.auth.response.LoginResponse;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.dto.auth.response.TokenResponse;
import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import com.bodytok.healthdiary.service.auth.jwt.JwtService;
import com.bodytok.healthdiary.service.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

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

        String jwtToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        //redis 저장
        JwtToken access = JwtToken.of(jwtToken, TokenType.ACCESS);
        JwtToken refresh = JwtToken.of(refreshToken, TokenType.REFRESH);

        jwtService.saveToken(access);
        jwtService.saveToken(refresh);


        TokenResponse tokenResponse = TokenResponse.of(jwtToken, refreshToken);
        UserResponse userResponse = UserResponse.from(userAccountDto);
        return new LoginResponse(tokenResponse,userResponse);
    }


    //TODO : accessToken 만료 전 refresh 요청 시 로그아웃 시키기, 현재 context 에 있는 유저와 비교하는 로직 추가
    public String refreshToken(String refreshToken) {
        JwtToken token = jwtService.getToken(refreshToken);
        if (token != null){
            if (token.getTokenType() == TokenType.REFRESH) {
                UserDetails userDetails = jwtUtil.getUserDetailsFromToken(token.getToken());
                if(userDetails != null){
                    //새 엑세스 토큰 발급 & redis에 저장
                    String jwt = jwtUtil.generateToken(userDetails);
                    jwtService.saveToken(JwtToken.of(jwt, TokenType.ACCESS));

                    //엑세스 토큰 반환
                    return jwtUtil.generateToken(userDetails);
                } else {
                    return "REFRESH_TOKEN_FAILED";
                }
            }
        }

        return "REFRESH_TOKEN_FAILED";
    }

    public void logout(String token, String refreshToken, CustomUserDetails authUser) {
        UserDetails userDetailsFromToken = jwtUtil.getUserDetailsFromToken(token);
        var userEmail = userDetailsFromToken.getUsername();
        if (!Objects.equals(userEmail, authUser.getUsername())){
            throw new AccessDeniedException("로그인된 유저 정보와 일치하지 않습니다.");
        }
        jwtService.deleteToken(token, refreshToken);
    }
}
