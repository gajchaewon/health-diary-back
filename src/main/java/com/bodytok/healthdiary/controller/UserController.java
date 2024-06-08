package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.userAccount.UserProfile;
import com.bodytok.healthdiary.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "User")
public class UserController {

    private final UserAccountService userAccountService;

    @GetMapping
    @Operation(summary = "유저 프로필 정보 조회")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class))
    })
    public UserProfile userProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(name = "userId", description = "조회하는 유저 ID - 없으면 내 프로필 조회")
            @RequestParam(name = "userId", required = false) Long userId
    ) {
        Long requestUserId = userId == null ? userDetails.getId() : userId;
        return userAccountService.getUserProfile(userDetails.getId(), requestUserId);
    }
}
