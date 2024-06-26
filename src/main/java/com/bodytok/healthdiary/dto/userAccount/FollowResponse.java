package com.bodytok.healthdiary.dto.userAccount;

import com.bodytok.healthdiary.domain.ProfileImage;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.FollowStatus;
import com.bodytok.healthdiary.dto.Image.ImageResponse;
import com.bodytok.healthdiary.dto.Image.ProfileImageDtoImpl;


public record FollowResponse(
        Long id,
        String email,
        String nickname,
        ImageResponse profileImage,
        FollowStatus followStatus
) {

    public static FollowResponse fromUserEntity(UserAccount userAccount, FollowStatus followStatus) {
        return new FollowResponse(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                ImageResponse.from(
                        ProfileImageDtoImpl.from(
                                userAccount.getProfileImage() == null ?
                                        new ProfileImage() : userAccount.getProfileImage()
                        )
                ),
                followStatus
        );
    }

}
