package com.bodytok.healthdiary.dto.userAccount;


import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.FollowStatus;
import com.bodytok.healthdiary.dto.UserAccountDto;

import java.util.Objects;

/**
 * 유저 id, 닉네임, 이메일
 * 팔로워 / 팔로잉 count
 * 커뮤니티 일지 count
 * 나의 팔로잉 여부
 */

public record UserProfile(
        Long id,
        String nickname,
        String email,
        Integer followerCount,
        Integer followingCount,
        Integer diaryCount,
        FollowStatus followStatus
) {

    public static UserProfile of(Long id, String nickname, String email, Integer followerCount, Integer followingCount, Integer diaryCount, FollowStatus followStatus){
        return new UserProfile(id, nickname, email, followerCount, followingCount, diaryCount, followStatus);
    }

    public static UserProfile toProfileInfo(UserAccount userAccount, Integer diaryCount, Long myId){
        boolean isMyProfile = Objects.equals(userAccount.getId(), myId);
        FollowStatus followStatus1 = null;
        if(!isMyProfile){
            // 유저의 팔로워 리스트에 나의 아이디가 있다면
           if( userAccount.getFollowerList().stream()
                   .anyMatch(f -> Objects.equals(f.getFollower().getId(), myId))) {
               followStatus1 = FollowStatus.FOLLOW;
           }else {
               followStatus1 = FollowStatus.NONE;
           }
        }else {
            followStatus1 = FollowStatus.SELF;
        }

        return of(
                userAccount.getId(),
                userAccount.getNickname(),
                userAccount.getEmail(),
                userAccount.getFollowerList().size(),
                userAccount.getFollowingList().size(),
                diaryCount,
                followStatus1
        );
    }

}
