package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.repository.diaryhashtag.DiaryHashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PersonalExerciseDiaryHashtagRepository extends
        JpaRepository<PersonalExerciseDiaryHashtag, Long>,
        DiaryHashtagRepositoryCustom
{
}