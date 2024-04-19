package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.QComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;


import java.util.List;


public interface CommentRepository extends
        JpaRepository<Comment, Long>,
        QuerydslPredicateExecutor<Comment>,
        QuerydslBinderCustomizer<QComment> {

    List<Comment> findByPersonalExerciseDiary_Id(Long diaryId);

    List<Comment> findByUserAccount_Id(Long userId);

    void deleteByIdAndUserAccount_Id(Long commentId, Long userId);

    @Override
    default void customize(QuerydslBindings bindings, QComment root) {
        bindings.excludeUnlistedProperties(true); // 선택적인 필드들만 검색 기능 동작하게, 다른 필드들은 제외
        bindings.including( root.content, root.createdAt); //원하는 필드 추가
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // -> like '%${value}%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // TODO : 시분초를 동일하게 넣어줘야 하기 때문에 나중에 바꿔야함
    }
}