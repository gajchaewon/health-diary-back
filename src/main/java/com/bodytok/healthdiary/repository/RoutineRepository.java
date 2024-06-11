package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.QRoutine;
import com.bodytok.healthdiary.domain.Routine;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends
        JpaRepository<Routine, Long>,
        QuerydslPredicateExecutor<Routine>,
        QuerydslBinderCustomizer<QRoutine>
{
    Optional<List<Routine>> findAllByUserAccount_Id(Long userId);

    @Override
    default void customize(QuerydslBindings bindings, QRoutine root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.routineName, root.memo);
        bindings.bind(root.routineName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.memo).first(StringExpression::containsIgnoreCase);
    }
}