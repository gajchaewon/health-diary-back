package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.QPersonalExerciseDiary;
import com.bodytok.healthdiary.repository.diary.PersonalExerciseDiaryRepositoryCustom;
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
        PersonalExerciseDiaryRepositoryCustom,
        QuerydslPredicateExecutor<PersonalExerciseDiary>, // 기본적으로 모든 필드에 대한 검색기능 추가
        QuerydslBinderCustomizer<QPersonalExerciseDiary> { // 디테일한 검색 기능 추가를 위해 사용 (부분 문자열 검색 등)

    //TODO : 모든 검색을 Containing 으로 하고 있으므로, 인덱스 사용 문제가 야기될 수 있다. 튜닝필요

    //커뮤니티용 (IsPublic = true)
    Page<PersonalExerciseDiary> findAllByIsPublicTrue(Pageable pageable);
    Page<PersonalExerciseDiary> findByUserAccount_IdAndIsPublicTrue(Long userId, Pageable pageable);
    Page<PersonalExerciseDiary> findByUserAccount_NicknameContainingAndIsPublicTrue(String nickname, Pageable pageable);

    //----------------------//

    //개인용
    Page<PersonalExerciseDiary> findByUserAccount_Id(Long userId, Pageable pageable);
    Page<PersonalExerciseDiary> findByUserAccount_IdAndCreatedAtBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QPersonalExerciseDiary root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title,root.content, root.isPublic, root.createdAt);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%{value}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
    }
}