package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.DiaryLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;


public interface DiaryLikeRepository extends
        JpaRepository<DiaryLike, Long>,
        QuerydslPredicateExecutor<DiaryLike>
{
    Optional<Page<DiaryLike>> findByUserAccount_Id(Long userId, Pageable pageable);
}
