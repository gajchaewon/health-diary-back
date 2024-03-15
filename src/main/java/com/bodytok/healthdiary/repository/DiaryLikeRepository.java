package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.DiaryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface DiaryLikeRepository extends
        JpaRepository<DiaryLike, Long>,
        QuerydslPredicateExecutor<DiaryLike>
{

}
