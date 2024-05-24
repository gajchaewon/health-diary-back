package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.QExercise;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ExerciseRepository extends
        JpaRepository<Exercise, Long>,
        QuerydslPredicateExecutor<Exercise>,
        QuerydslBinderCustomizer<QExercise>
{

    @Override
    default void customize(QuerydslBindings bindings, QExercise root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.exerciseName,root.description);
        bindings.bind(root.exerciseName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.description).first(StringExpression::containsIgnoreCase);
    }
}