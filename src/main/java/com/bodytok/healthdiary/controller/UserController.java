package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.Image.ImageResponse;
import com.bodytok.healthdiary.dto.userAccount.response.UserProfile;
import com.bodytok.healthdiary.service.ProfileImageService;
import com.bodytok.healthdiary.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "User")
public class UserController {

    private final UserAccountService userAccountService;
    private final ProfileImageService profileImageService;
    @GetMapping
    @Operation(summary = "유저 프로필 정보 조회")
    public ResponseEntity<UserProfile> userProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(name = "userId", description = "조회하는 유저 ID - 없으면 내 프로필 조회")
            @RequestParam(name = "userId", required = false) Long userId
    ) {
        Long requestUserId = userId == null ? userDetails.getId() : userId;
        var response = userAccountService.getUserProfile(userDetails.getId(), requestUserId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 프로필 사진 업로드", description = "이미지 File 을 저장 - MultipartFile")
    @ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 이미지 ID와 URL 반환함")
    public ResponseEntity<ImageResponse> uploadProfileImage(
            @RequestParam("file") MultipartFile file) throws Exception
    {
        var response = ImageResponse.from(profileImageService.uploadProfileImage(file));
        return ResponseEntity.ok().body(response);
    }
}
