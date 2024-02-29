package com.bodytok.healthdiary.repository;


import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommunityExerciseDiaryRepositoryCustom {
    Page<PersonalExerciseDiary> findAllPersonalDiariesInCommunity(Long communityDiaryId, Pageable pageable);
}
