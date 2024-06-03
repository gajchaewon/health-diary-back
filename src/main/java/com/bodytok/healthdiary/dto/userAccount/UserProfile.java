package com.bodytok.healthdiary.dto.userAccount;


import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;

/**
 * 유저 id, 닉네임, 이메일
 * 팔로워 / 팔로잉 count
 * 커뮤니티 일지 count
 */

public record UserProfile(
        Long id,
        String nickname,
        String email,
        Integer followerCount,
        Integer followingCount,
        Integer diaryCount
) {

    public static UserProfile of(Long id, String nickname, String email, Integer followerCount, Integer followingCount, Integer diaryCount){
        return new UserProfile(id, nickname, email, followerCount, followingCount, diaryCount);
    }

    public static UserProfile toProfileInfo(UserAccount userAccount, Integer diaryCount){
        return of(
                userAccount.getId(),
                userAccount.getNickname(),
                userAccount.getEmail(),
                userAccount.getFollowerList().size(),
                userAccount.getFollowingList().size(),
                diaryCount
        );
    }

}
