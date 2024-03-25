package com.bodytok.healthdiary.dto;

import com.bodytok.healthdiary.domain.UserAccount;

import java.time.LocalDateTime;

public record FollowResponse(
        Long id,
        String email,
        String nickname,
        Byte[] profileImage,
        String followStatus
) {

    public static FollowResponse fromUserEntity(UserAccount userAccount, String followStatus){
        return new FollowResponse(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getProfileImage(),
                followStatus
        );
    }

}
