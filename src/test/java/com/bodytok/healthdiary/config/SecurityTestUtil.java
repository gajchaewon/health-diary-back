package com.bodytok.healthdiary.config;

import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityTestUtil {
    public static CustomUserDetails setUpMockUser() {
        CustomUserDetails userDetails = CustomUserDetails.of(1L, "210@mail.com", "이원영", "password1234!");

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return userDetails;
    }
}
