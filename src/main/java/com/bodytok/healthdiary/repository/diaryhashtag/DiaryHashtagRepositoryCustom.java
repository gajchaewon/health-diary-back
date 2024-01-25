package com.bodytok.healthdiary.repository.diaryhashtag;

import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryHashtagRepositoryCustom {

    Page<PersonalExerciseDiaryHashtag> findDiariesByHashtagId(Long hashtagId, Pageable pageable);
}
