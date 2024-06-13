package com.bodytok.healthdiary.service.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtUtil {
    // Create a key string with 256 bits
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserDetailsService userDetailsService;
    private final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();

    @Value("${jwt.access-token.expiration}")
    long accessExpiration;
    @Value("${jwt.refresh-token.expiration}")
    long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private SecretKey getSignInKey() {
        return SECRET_KEY;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, accessExpiration);

    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Cookie createCookie(String refreshToken) {
        String cookieName = "refreshToken";
        Cookie cookie = new Cookie(cookieName, refreshToken);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        return cookie;
    }

    public UserDetails getUserDetailsFromToken(String token) {
        String username = extractUsername(token);
        if (username != null) {
            try {
                return userDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
