package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.userAccount.UserProfile;
import com.bodytok.healthdiary.service.UserAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "User")
public class UserController {

    private final UserAccountService userAccountService;

    @GetMapping
    public UserProfile userProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return userAccountService.getUserProfile(userDetails.getId());
    }
}
