package com.bodytok.healthdiary.repository.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.QPersonalExerciseDiary;
import com.bodytok.healthdiary.domain.QPersonalExerciseDiaryHashtag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PersonalExerciseDiaryRepositoryCustomImpl implements PersonalExerciseDiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest httpServletRequest;

    @Override
    public Page<PersonalExerciseDiary> findByDiaryHashtag(Long userId, String hashtag, Pageable pageable) {
        QPersonalExerciseDiary diary = QPersonalExerciseDiary.personalExerciseDiary;
        QPersonalExerciseDiaryHashtag diaryHashtag = QPersonalExerciseDiaryHashtag.personalExerciseDiaryHashtag;
        //isPublic 조건
        BooleanExpression isPublicCondition = isMyRequestForPublic(httpServletRequest, diary);

        JPAQuery<PersonalExerciseDiary> query = queryFactory.selectFrom(diary);

        //유저 아이디를 넣으면 내 소유 다이어리를 조회하는 조건 추가
        //미세한 차이일 수도 있으나 유저아이디로 먼저 필터링 후 innerJoin 하는 것이 좋을 것 같음.
        if (userId != null) {
            query = query.where(diary.userAccount.id.eq(userId));

        }

        query = query.innerJoin(diary.diaryHashtags, diaryHashtag)
                .where(diaryHashtag.hashtag.hashtag.eq(hashtag));

        if (isPublicCondition != null) {
            query = query.where(isPublicCondition);
        }
        List<PersonalExerciseDiary> result = query.fetchJoin().distinct().fetch();
        long count = result.size();
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<PersonalExerciseDiary> findByTitleContaining(Long userId, String title, Pageable pageable) {
        QPersonalExerciseDiary diary = QPersonalExerciseDiary.personalExerciseDiary;
        BooleanExpression isPublicCondition = isMyRequestForPublic(httpServletRequest, diary);
        JPAQuery<PersonalExerciseDiary> query = queryFactory.selectFrom(diary)
                .where(diary.title.containsIgnoreCase(title));

        if (userId != null) {
            query = query.where(diary.userAccount.id.eq(userId));
        }
        if (isPublicCondition != null) {
            query = query.where(isPublicCondition);
        }
        List<PersonalExerciseDiary> result = query.fetch();
        long count = result.size();
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<PersonalExerciseDiary> findByContentContaining(Long userId, String content, Pageable pageable) {
        QPersonalExerciseDiary diary = QPersonalExerciseDiary.personalExerciseDiary;
        BooleanExpression isPublicCondition = isMyRequestForPublic(httpServletRequest, diary);
        JPAQuery<PersonalExerciseDiary> query = queryFactory.selectFrom(diary);

        if (userId != null) {
            query = query.where(diary.userAccount.id.eq(userId));
        }
        query = query.where(diary.content.containsIgnoreCase(content));
        //
        if (isPublicCondition != null) {
            query = query.where(isPublicCondition);
        }
        List<PersonalExerciseDiary> result = query.fetch();
        long count = result.size();
        return new PageImpl<>(result, pageable, count);
    }


    //나의 다이어리를 검색 OR 다른 유저의 다이어리를 검색하는지 체크
    //결과에 따라 isPublic 조건을 조절함
    private BooleanExpression isMyRequestForPublic(HttpServletRequest request, QPersonalExerciseDiary diary) {
        if (request.getRequestURI().equals("/diaries/my")) {
            return null;
        }
        return diary.isPublic.isTrue();
    }
}
