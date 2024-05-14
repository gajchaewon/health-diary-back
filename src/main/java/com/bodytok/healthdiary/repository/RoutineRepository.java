package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface RoutineRepository extends
        JpaRepository<Routine, Long>,
        QuerydslPredicateExecutor<Routine>,
        QuerydslBinderCustomizer<QRoutine>
{
}