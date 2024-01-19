package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PersonalExerciseDiaryHashtagRepository extends JpaRepository<PersonalExerciseDiaryHashtag, Long> {
}