package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.Hashtag;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.QHashtag;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface HashtagRepository extends
        JpaRepository<Hashtag, Long>,
        QuerydslPredicateExecutor<Hashtag>,
        QuerydslBinderCustomizer<QHashtag>
{

    Hashtag findByHashtagContaining(String Hashtag);


    @Override
    default void customize(QuerydslBindings bindings, QHashtag root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.hashtag);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // like '%{value}%'
    }
}