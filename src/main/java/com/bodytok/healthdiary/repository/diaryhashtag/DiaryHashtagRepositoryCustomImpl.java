package com.bodytok.healthdiary.repository.diaryhashtag;

import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.domain.QPersonalExerciseDiaryHashtag;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class DiaryHashtagRepositoryCustomImpl extends QuerydslRepositorySupport implements DiaryHashtagRepositoryCustom {

    public DiaryHashtagRepositoryCustomImpl() {
        super(PersonalExerciseDiaryHashtag.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonalExerciseDiaryHashtag> findDiariesByHashtagId(Long hashtagId, Pageable pageable) {
        QPersonalExerciseDiaryHashtag qDiaryHashtag = QPersonalExerciseDiaryHashtag.personalExerciseDiaryHashtag;

        JPQLQuery<PersonalExerciseDiaryHashtag> query = from(qDiaryHashtag)
                .where(qDiaryHashtag.hashtag.id.eq(hashtagId));

        List<PersonalExerciseDiaryHashtag> diaries =
                Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();

        long total = query.fetchCount();

        return new PageImpl<>(diaries, pageable, total);
    }
}
