package com.bodytok.healthdiary.config.security;

import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;


    @Cacheable(value = "AuthCacheStore",key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">>>CustomUserDetailService - username :{}", username);
        return userAccountRepository.findByEmail(username)
                .map(UserAccountDto::from)
                .map(CustomUserDetails::from)
                .orElseThrow(() -> new CustomBaseException(CustomError.USER_NOT_FOUND));
    }
}
