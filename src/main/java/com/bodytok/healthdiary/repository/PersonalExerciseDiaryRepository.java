package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.QPersonalExerciseDiary;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.time.LocalDateTime;

public interface PersonalExerciseDiaryRepository extends
        JpaRepository<PersonalExerciseDiary, Long>,
        QuerydslPredicateExecutor<PersonalExerciseDiary>, // 기본적으로 모든 필드에 대한 검색기능 추가
        QuerydslBinderCustomizer<QPersonalExerciseDiary> { // 디테일한 검색 기능 추가를 위해 사용 (부분 문자열 검색 등)

    //TODO : 모든 검색을 Containing 으로 하고 있으므로, 인덱스 사용 문제가 야기될 수 있다. 튜닝필요
    Page<PersonalExerciseDiary> findByTitleContaining(String title, Pageable pageable);
    Page<PersonalExerciseDiary> findByContentContaining(String content, Pageable pageable);
    Page<PersonalExerciseDiary> findByUserAccount_Id(Long Id, Pageable pageable);
    Page<PersonalExerciseDiary> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<PersonalExerciseDiary> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QPersonalExerciseDiary root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.isPublic, root.totalExTime, root.createdAt);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%{value}%'
    }
}