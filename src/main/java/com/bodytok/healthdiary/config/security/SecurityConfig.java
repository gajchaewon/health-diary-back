package com.bodytok.healthdiary.config.security;


import com.bodytok.healthdiary.filter.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
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
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
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
                                .requestMatchers("auth/login/**").permitAll()
                                .requestMatchers("auth/sign-up/**").permitAll()
                                .requestMatchers("auth/refresh-token/**").permitAll() //refresh-token 요청
                                .requestMatchers(HttpMethod.GET,"diaries/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "community/**").permitAll() //커뮤니티 다이어리 가져오기
                                .anyRequest().authenticated()
                )
                //Authentication Entry Point -> Exception Handler
                .exceptionHandling(
                        config -> config.authenticationEntryPoint(authenticationEntryPoint)
                )
                // Set Session policy = STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
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
