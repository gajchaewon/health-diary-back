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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        userAccountRepository.findByEmail(request.getEmail()).ifPresent(userAccount -> {
            throw new RuntimeException("User already exists -> User's Email : " + userAccount.getEmail());
        });
        CustomUserDetails userDetails = CustomUserDetails.of(
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword()),
                request.getProfileImage()
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
        CustomUserDetails userDetails = userAccountRepository.findByEmail(request.email())
                .map(UserAccountDto::from)
                .map(CustomUserDetails::from)
                .orElseThrow();
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        TokenResponse tokenResponse = TokenResponse.of(jwtToken, refreshToken);
        UserResponse userResponse = UserResponse.from(userDetails.toDto());
        return new LoginResponse(tokenResponse,userResponse);
    }

    public String refreshToken(String refreshToken) throws IOException {
        // 리프레시 토큰으로 유저 데이터 추출
        UserDetails userDetails = jwtService.getUserDetailsFromToken(refreshToken);

        // UserDetails가 유효하다면 새로운 accessToken과 refreshToken을 발급
        if (userDetails != null) {
            String newAccessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            // 새로 발급받은 refreshToken은 클라이언트에게 반환하지 않는다
            return newAccessToken;
        } else {
            // refreshToken이 유효하지 않은 경우 예외 처리 또는 다른 로직을 수행
//            throw new RuntimeException("REFRESH_TOKEN_FAILED");
            return "REFRESH_TOKEN_FAILED";
        }
    }
}
