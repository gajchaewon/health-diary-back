package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.ExerciseRoutine;
import com.bodytok.healthdiary.domain.QExerciseRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface ExerciseRoutineRepository extends
        JpaRepository<ExerciseRoutine, Long>,
        QuerydslPredicateExecutor<ExerciseRoutine>,
        QuerydslBinderCustomizer<QExerciseRoutine> {
}