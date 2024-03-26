package com.bodytok.healthdiary.service.auth;

import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.auth.request.AuthenticationRequest;
import com.bodytok.healthdiary.dto.auth.request.RegisterRequest;
import com.bodytok.healthdiary.dto.auth.response.LoginResponse;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.dto.auth.response.TokenResponse;
import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import com.bodytok.healthdiary.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        userAccountRepository.findByEmail(request.email()).ifPresent(userAccount -> {
            throw new DuplicateKeyException("User already exists -> User's Email : " + userAccount.getEmail());
        });
        CustomUserDetails userDetails = CustomUserDetails.of(
                request.email(),
                request.nickname(),
                passwordEncoder.encode(request.password())
        );
        UserAccount user = CustomUserDetails.toUserEntity(userDetails);
        userAccountRepository.save(user);
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new RegisterResponse(jwtToken, refreshToken);
    }


    public LoginResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Optional<UserAccountDto> userAccountDto = userAccountRepository.findByEmail(request.email())
                .map(UserAccountDto::from);
        CustomUserDetails userDetails = userAccountDto
                .map(CustomUserDetails::from)
                .orElseThrow();
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        TokenResponse tokenResponse = TokenResponse.of(jwtToken, refreshToken);
        UserResponse userResponse = UserResponse.from(userAccountDto.get());
        return new LoginResponse(tokenResponse,userResponse);
    }

    public String refreshToken(String refreshToken) throws IOException {
        UserDetails userDetails = jwtService.getUserDetailsFromToken(refreshToken);
        if(userDetails != null){
            String newAccessToken = jwtService.generateToken(userDetails);
            log.info("새로운 엑세스 토큰 : {} " , newAccessToken);
            return newAccessToken;
        } else {
            // refreshToken이 유효하지 않은 경우 예외 처리 또는 다른 로직을 수행
//            throw new RuntimeException("REFRESH_TOKEN_FAILED");
            return "REFRESH_TOKEN_FAILED";
        }
    }
}
