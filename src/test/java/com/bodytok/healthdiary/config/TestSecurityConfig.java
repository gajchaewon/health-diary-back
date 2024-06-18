package com.bodytok.healthdiary.config;


import com.bodytok.healthdiary.filter.jwt.JwtAuthenticationFilter;
import com.bodytok.healthdiary.filter.jwt.JwtExceptionFilter;
import com.bodytok.healthdiary.service.auth.jwt.JwtService;
import com.bodytok.healthdiary.service.auth.jwt.JwtUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@EnableWebSecurity
@Import({JwtAuthenticationFilter.class,JwtExceptionFilter.class })
public class TestSecurityConfig {
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "images/**").permitAll() //정적리소스 이미지 경로
                            .requestMatchers(HttpMethod.GET, "community/**").permitAll() //커뮤니티 다이어리 가져오기
                            .requestMatchers(HttpMethod.GET, "diaries/my").authenticated()
                            .requestMatchers(HttpMethod.GET, "diaries/{diaryId}").permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}