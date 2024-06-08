package com.bodytok.healthdiary.dto;

import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.FollowStatus;


public record FollowResponse(
        Long id,
        String email,
        String nickname,
        Byte[] profileImage,
        FollowStatus followStatus
) {

    public static FollowResponse fromUserEntity(UserAccount userAccount, FollowStatus followStatus){
        return new FollowResponse(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getProfileImage(),
                followStatus
        );
    }

}
