package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.ExerciseRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ExerciseRoutineRepository extends
        JpaRepository<ExerciseRoutine, Long>,
        QuerydslPredicateExecutor<ExerciseRoutine> {
}