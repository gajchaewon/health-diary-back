package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.userAccount.FollowResponse;
import com.bodytok.healthdiary.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users/follow")
@Tag(name = "Follow")
public class FollowController {
    private final FollowService followService;


    //팔로우
    @PostMapping("/{userId}")
    @Operation(summary = "유저 팔로우 신청")
    public ResponseEntity<FollowResponse> follow(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ResponseEntity.ok().body(followService.follow(userDetails, userId));
    }

    // 유저의 팔로잉 조회
    @GetMapping("/{userId}/following")
    @Operation(summary = "유저 팔로우 조회")
    public ResponseEntity<List<FollowResponse>> getFollowingList(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok().body(followService.followingList(userDetails, userId));
    }

    // 유저의 팔로워 조회
    @GetMapping("/{userId}/follower")
    @Operation(summary = "유저 팔로워 조회")
    public ResponseEntity<List<FollowResponse>> getFollowerList(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        //유저를 팔로우 하는 유저들 중 내가 팔로잉하고 있는지 확인하기 위함
        return ResponseEntity.ok().body(followService.followerList(userDetails, userId));
    }

    // 팔로우 끊기
    @DeleteMapping("/{userId}")
    @Operation(summary = "팔로잉 취소")
    public ResponseEntity<FollowResponse> deleteFollow(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        return ResponseEntity.ok().body(followService.unFollow(userDetails, userId));
    }
}