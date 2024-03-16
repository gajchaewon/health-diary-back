package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.repository.diaryhashtag.DiaryHashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalExerciseDiaryHashtagRepository extends
        JpaRepository<PersonalExerciseDiaryHashtag, Long>,
        DiaryHashtagRepositoryCustom
{
}