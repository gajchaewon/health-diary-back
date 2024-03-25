package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends
        JpaRepository<Follow, Long>
{
    Boolean existsFollowByFollowerIdAndFollowingId(Long fromUserId, Long toUserId);
    Optional<Follow> findByFollowerIdAndFollowingId(Long fromUserId, Long toUserId);
}
