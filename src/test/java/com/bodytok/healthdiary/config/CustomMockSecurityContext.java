package com.bodytok.healthdiary.config;

import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class CustomMockSecurityContext implements WithSecurityContextFactory<CustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(CustomMockUser annotation) {

        var principal = CustomUserDetails.of(annotation.email(), annotation.nickname(), annotation.userPassword());
        var authToken = new UsernamePasswordAuthenticationToken(principal, annotation.userPassword(), principal.getAuthorities());
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        return context;
    }
}
