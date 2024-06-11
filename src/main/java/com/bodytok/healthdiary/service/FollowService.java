package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Follow;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.constant.FollowStatus;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.FollowResponse;
import com.bodytok.healthdiary.exepction.FollowException;
import com.bodytok.healthdiary.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class FollowService {
    private final UserAccountService userAccountService;
    private final FollowRepository followRepository;

    // 팔로우
    public FollowResponse follow(CustomUserDetails userDetails, Long followingId) {
        UserAccount followers = userAccountService.getUserById(userDetails.getId());
        UserAccount following = userAccountService.getUserById(followingId);

        // 자기 자신은 팔로우 X
        if (followers.getId().equals(following.getId())) {
            throw new FollowException(HttpStatus.BAD_REQUEST, "자기 자신을 follow할 수 없습니다.");
        }
        // 중복 팔로우 x
        if (followRepository.existsFollowByFollowerIdAndFollowingId(followers.getId(), following.getId())) {
            throw new FollowException(HttpStatus.BAD_REQUEST, "이미 follow했습니다.");
        }
        Follow follow = Follow.of(followers, following);

        followRepository.save(follow);
        return FollowResponse.fromUserEntity(following, FollowStatus.FOLLOW);
    }

    // 유저가 팔로잉 하고 있는 유저들 조회 (유저 -> 유저)
    @Transactional(readOnly = true)
    public List<FollowResponse> followingList(CustomUserDetails userDetails, Long userId) {
        Long myId = userDetails.getId();
        boolean isMyId = myId.equals(userId);

        UserAccount user = userAccountService.getUserById(userId);
        return user.getFollowingList().stream()
                .map(follow -> {
                    var followingUser = follow.getFollowing();
                    FollowStatus association = isMyId ? FollowStatus.FOLLOW : association(myId, userId);
                    return FollowResponse.fromUserEntity(followingUser, association);
                })
                .collect(Collectors.toList());
    }


    //TODO : 중복되는 코드가 많이 보인다. -> request param 을 받아서 동적으로 처리하게 리팩토링하자
    //유저를 팔로잉 하고 있는 유저들 조회 (유저 <- 유저)
    @Transactional(readOnly = true)
    public List<FollowResponse> followerList(CustomUserDetails userDetails, Long userId) {
        Long myId = userDetails.getId();
        boolean isMyId = myId.equals(userId);

        UserAccount user = userAccountService.getUserById(userId);
        return user.getFollowerList().stream()
                .map(follow -> {
                    var usersFollower = follow.getFollower();
                    FollowStatus association = isMyId ? FollowStatus.FOLLOW :association(myId, userId);
                    return FollowResponse.fromUserEntity(usersFollower, association);
                })
                .collect(Collectors.toList());
    }

    public FollowResponse unFollow(CustomUserDetails userDetails, Long userId) {

        final Long followerId = userDetails.getId();

        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, userId)
                .orElseThrow(() -> new FollowException(HttpStatus.NOT_FOUND, "팔로잉 하는 유저가 아닙니다."));

        UserAccount user = follow.getFollowing();

        followRepository.delete(follow);

        return FollowResponse.fromUserEntity(user, FollowStatus.NONE);
    }


    public FollowStatus association(Long myId, Long userId) {
        if (userId.equals(myId)){
            return FollowStatus.SELF;
        }
        if (followRepository.existsFollowByFollowerIdAndFollowingId(myId, userId)){
            return FollowStatus.FOLLOW;

        }
        return FollowStatus.NONE;
    }
}
