package com.bodytok.healthdiary.domain;

import com.bodytok.healthdiary.domain.constant.TokenType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;


@Getter
@ToString
@RedisHash(value = "authToken")
public class JwtToken {

    @Id
    private String token;

    @Setter
    private TokenType tokenType; // 토큰의 타입 (예: access, refresh)

    @Setter
    @TimeToLive(unit = TimeUnit.MILLISECONDS) //token 타입에 따른 만료시간
    private Long expiration; // 토큰의 만료 시간

    protected JwtToken() {
    }

    private JwtToken(String token, TokenType tokenType, Long expiration) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiration = expiration;
    }
    private JwtToken(String token, TokenType tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public static JwtToken of(String token, TokenType tokenType) {
        return new JwtToken(token, tokenType,null);
    }


}