package com.bodytok.healthdiary.config.security;


import com.bodytok.healthdiary.config.security.handler.CustomAccessDeniedHandler;
import com.bodytok.healthdiary.config.security.handler.CustomAuthenticationEntryPoint;
import com.bodytok.healthdiary.filter.jwt.JwtAuthenticationFilter;
import com.bodytok.healthdiary.filter.jwt.JwtExceptionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, JwtExceptionFilter jwtExceptionFilter, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtExceptionFilter = jwtExceptionFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authForOpenApiDocs -> authForOpenApiDocs
                                .requestMatchers("v2/api-docs").permitAll()
                                .requestMatchers("v3/api-docs").permitAll()
                                .requestMatchers("v3/api-docs/**").permitAll()
                                .requestMatchers("swagger-resources").permitAll()
                                .requestMatchers("swagger-resources/**").permitAll()
                                .requestMatchers("configuration/ui").permitAll()
                                .requestMatchers("configuration/security").permitAll()
                                .requestMatchers("swagger-ui/**").permitAll()
                                .requestMatchers("webjars/**").permitAll()
                                .requestMatchers("swagger-ui.html").permitAll()

                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "images/**").permitAll() //정적리소스 이미지 경로
                                .requestMatchers(HttpMethod.GET, "community/**").permitAll() //커뮤니티 다이어리 가져오기
                                .requestMatchers(HttpMethod.GET, "diaries/my").authenticated()
                                .requestMatchers(HttpMethod.GET, "diaries/{diaryId}").permitAll()
                                .anyRequest().authenticated()
                )
                //Authentication Entry Point -> Exception Handler
                .exceptionHandling(
                        config -> config.authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                // Set Session policy = STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .build();
    }


    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 클라이언트가 쿠키를 전송할 수 있도록 허용
        config.addAllowedOrigin("http://localhost:3000"); // 특정 출처 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }
}
