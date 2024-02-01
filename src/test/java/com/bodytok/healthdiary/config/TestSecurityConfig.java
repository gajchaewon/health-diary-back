package com.bodytok.healthdiary.config;


import com.bodytok.healthdiary.config.security.AuthConfig;
import com.bodytok.healthdiary.config.security.SecurityConfig;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@Import({SecurityConfig.class, AuthConfig.class})
public class TestSecurityConfig {
    // security 적용에서 테스트를 위한 user 레포지토리를 빈으로 등록하고, 테스트 계정을 하나 생성해준다.
    @MockBean
    private UserAccountRepository userAccountRepository;


    @BeforeTestMethod //각 테스트 메소드가 실행되기 직전에 실행시켜줌
    public void securitySetUp() {
        given(userAccountRepository.findById(anyLong())).willReturn(Optional.of(UserAccount.of(
                        "test@email.com",
                        "testuser",
                        "testpwd",
                        null
                )
        ));
    }
}