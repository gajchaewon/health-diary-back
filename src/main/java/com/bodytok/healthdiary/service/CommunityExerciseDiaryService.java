package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.CommunityExerciseDiary;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommunityExerciseDiaryService {

    private final CommunityExerciseDiaryRepository communityExerciseDiaryRepository;

    public void savePublicDiary(PersonalExerciseDiary diary){
        communityExerciseDiaryRepository.save(CommunityExerciseDiary.of(diary));
    }
}
