package com.bodytok.healthdiary.config;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomMockSecurityContext.class)
public @interface CustomMockUser {

    String nickname() default "이원영";
    String email() default "210@mail.com";

    String userPassword() default "password1234!";
}
