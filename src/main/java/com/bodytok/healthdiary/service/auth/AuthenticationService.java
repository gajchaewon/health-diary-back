package com.bodytok.healthdiary.service.auth;

import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.JwtToken;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.auth.request.AuthenticationRequest;
import com.bodytok.healthdiary.dto.auth.request.RegisterRequest;
import com.bodytok.healthdiary.dto.auth.response.AuthenticationResponse;
import com.bodytok.healthdiary.dto.auth.response.CustomAuthStatus;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import com.bodytok.healthdiary.service.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
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
        return new AuthenticationResponse(jwtToken, refreshToken);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        UserDetails userDetails = userAccountRepository.findByEmail(request.email())
                .map(UserAccountDto::from)
                .map(CustomUserDetails::from)
                .orElseThrow();
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new AuthenticationResponse(jwtToken,refreshToken);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refresh;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refresh = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refresh);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(refresh, userDetails)) {
                String accessToken = jwtService.generateToken(userDetails);
                AuthenticationResponse res = new AuthenticationResponse(accessToken, refresh);
                new ObjectMapper().writeValue(response.getOutputStream(), res);
            }
        }
    }
}
