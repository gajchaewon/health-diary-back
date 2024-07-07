package com.bodytok.healthdiary.service.auth.jwt;

import com.bodytok.healthdiary.domain.constant.TokenType;
import com.bodytok.healthdiary.domain.JwtToken;
import com.bodytok.healthdiary.repository.jwt.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class JwtService {

    private final JwtTokenRepository jwtTokenRepository;

    @Value("${jwt.access-token.expiration}")
    long accessExpiration;

    @Value("${jwt.refresh-token.expiration}")
    long refreshExpiration;
    public JwtToken getToken(String token) {
        return jwtTokenRepository.findById(token).orElse(null);
    }


    public void saveToken(JwtToken token) {
        //access 인지 refresh 인지 구분해서 expiration 저장
        long expirationTime = (token.getTokenType() == TokenType.ACCESS) ? accessExpiration : refreshExpiration;

        token.setExpiration(expirationTime);
        jwtTokenRepository.save(token);
    }

    public void deleteToken(String token){
        jwtTokenRepository.deleteById(token);
    }
}