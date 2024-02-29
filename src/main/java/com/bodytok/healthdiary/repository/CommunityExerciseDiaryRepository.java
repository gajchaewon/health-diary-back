package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.CommunityExerciseDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface CommunityExerciseDiaryRepository extends
        JpaRepository<CommunityExerciseDiary, Long>,
        QuerydslPredicateExecutor<CommunityExerciseDiary>
{
}
