package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalExerciseDiaryHashtagRepository extends
        JpaRepository<PersonalExerciseDiaryHashtag, Long>
{
}