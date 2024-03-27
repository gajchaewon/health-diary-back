package com.bodytok.healthdiary.repository.diary;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonalExerciseDiaryRepositoryCustom {

    Page<PersonalExerciseDiary> findByDiaryHashtag(Long userId, String hashtag, Pageable pageable);

    Page<PersonalExerciseDiary> findByTitleContaining(Long userId, String title, Pageable pageable);

    Page<PersonalExerciseDiary> findByContentContaining(Long userId, String content, Pageable pageable);
}
