package com.bodytok.healthdiary.config.security;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final UserAccountRepository userAccountRepository;

    @Bean
    public UserDetailsService userDetailsService(){

        return username -> userAccountRepository.findByEmail(username)
                .map(UserAccountDto::from)
                .map(CustomUserDetails::from)
                .orElseThrow(() -> new CustomBaseException(CustomError.USER_NOT_FOUND));
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
