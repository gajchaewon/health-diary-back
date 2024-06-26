package com.bodytok.healthdiary.dto.userAccount.response;


import com.bodytok.healthdiary.domain.ProfileImage;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.FollowStatus;
import com.bodytok.healthdiary.dto.Image.ImageResponse;
import com.bodytok.healthdiary.dto.Image.ProfileImageDtoImpl;
import lombok.Builder;

import java.util.Objects;

/**
 * 유저 id, 닉네임, 이메일
 * 팔로워 / 팔로잉 count
 * 커뮤니티 일지 count
 * 프로필 이미지
 * 나의 팔로잉 여부
 */
@Builder
public record UserProfile(
        Long id,
        String nickname,
        String email,
        ImageResponse profileImage,
        Integer followerCount,
        Integer followingCount,
        Integer diaryCount,
        FollowStatus followStatus
) {


    public static UserProfile toProfileInfo(UserAccount userAccount, Integer diaryCount, Long myId){
        boolean isMyProfile = Objects.equals(userAccount.getId(), myId);
        FollowStatus association = null;
        if(!isMyProfile){
            // 유저의 팔로워 리스트에 나의 아이디가 있다면
           if( userAccount.getFollowerList().stream()
                   .anyMatch(f -> Objects.equals(f.getFollower().getId(), myId))) {
               association = FollowStatus.FOLLOW;
           }else {
               association = FollowStatus.NONE;
           }
        }else {
            association = FollowStatus.SELF;
        }

        return UserProfile.builder()
                .id(userAccount.getId())
                .nickname(userAccount.getNickname())
                .email(userAccount.getEmail())
                .profileImage(
                        ImageResponse.from(
                                ProfileImageDtoImpl.from(
                                        userAccount.getProfileImage() == null ?
                                        new ProfileImage() : userAccount.getProfileImage()))
                )
                .followerCount(userAccount.getFollowerList().size())
                .followingCount(userAccount.getFollowingList().size())
                .diaryCount(diaryCount)
                .followStatus(association)
                .build();
    }

}
