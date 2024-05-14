package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.QExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface ExerciseRepository extends
        JpaRepository<Exercise, Long>,
        QuerydslPredicateExecutor<Exercise>,
        QuerydslBinderCustomizer<QExercise> {
}