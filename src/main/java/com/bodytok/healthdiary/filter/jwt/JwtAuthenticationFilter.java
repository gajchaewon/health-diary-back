package com.bodytok.healthdiary.filter.jwt;

import com.bodytok.healthdiary.domain.JwtToken;
import com.bodytok.healthdiary.domain.constant.ErrorMessage;
import com.bodytok.healthdiary.domain.constant.TokenType;
import com.bodytok.healthdiary.service.jwt.JwtService;
import com.bodytok.healthdiary.service.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    private final AntPathRequestMatcher[] permitAllMatchers = {
            new AntPathRequestMatcher("/diaries", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/community", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/v2/api-docs"),
            new AntPathRequestMatcher("/v3/api-docs"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-resources"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/ui"),
            new AntPathRequestMatcher("/configuration/security"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger-ui.html")
    };


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;


        try {
            log.info("요청 정보\n->{}\n->{}", request.getMethod(), request.getRequestURI());
            if (isPermitAllRequest(request)) { // jwt 인증이 필요없는 요청 검증 없이 넘기기
                filterChain.doFilter(request, response);
                return;
            }
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtUtil.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                JwtToken tokenFromRedis = jwtService.getToken(jwt);
                boolean isAccessToken = tokenFromRedis != null && tokenFromRedis.getTokenType() == TokenType.ACCESS;
                if (jwtUtil.isTokenValid(jwt, userDetails) && isAccessToken) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new SignatureException("Token is invalid");
                }
            }
        } catch (SignatureException e) {
            log.info("SignatureException");
            throw new JwtException(ErrorMessage.WRONG_TYPE_TOKEN.getError());
        } catch (MalformedJwtException e) {
            log.info("MalformedJwtException");
            throw new JwtException(ErrorMessage.UNSUPPORTED_TOKEN.getError());
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException");
            throw new JwtException(ErrorMessage.EXPIRED_TOKEN.getError());
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException");
            throw new JwtException(ErrorMessage.UNKNOWN_ERROR.getError());
        } catch(JwtException | UsernameNotFoundException exception){
            log.error("JwtAuthentication Authentication Exception Occurs! - {}",exception.getClass());
            throw new JwtException(ErrorMessage.UNKNOWN_ERROR.getError());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPermitAllRequest(HttpServletRequest request) {
        return Arrays.stream(permitAllMatchers)
                .anyMatch(matcher -> matcher.matches(request));
    }
}
