package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryWithCommentDto;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PersonalExerciseDiaryService {

    private final PersonalExerciseDiaryRepository diaryRepository;
    private final UserAccountRepository userAccountRepository;
    @Transactional(readOnly = true)
    public Page<PersonalExerciseDiaryDto> getAllDiaries(Pageable pageable) {
        return diaryRepository.findAll(pageable).map(PersonalExerciseDiaryDto::from);
    }

    @Transactional(readOnly = true)
    public PersonalExerciseDiaryDto getDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .map(PersonalExerciseDiaryDto::from)
                .orElseThrow(() -> new EntityNotFoundException("다이어리가 없습니다. - diaryId : "+ diaryId));
    }

    @Transactional(readOnly = true)
    public PersonalExerciseDiaryWithCommentDto getDiaryWithComments(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .map(PersonalExerciseDiaryWithCommentDto::from)
                .orElseThrow(() -> new EntityNotFoundException("다이어리가 없습니다. - diaryId : "+ diaryId));
    }


    public PersonalExerciseDiaryDto saveDiary(PersonalExerciseDiaryDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());
        PersonalExerciseDiary diary = diaryRepository.save(dto.toEntity(userAccount));
        return PersonalExerciseDiaryDto.from(diary);
    }

    public void updateDiary(PersonalExerciseDiaryDto dto) {
        try {
            PersonalExerciseDiary diary = diaryRepository.getReferenceById(dto.id());
            if (dto.title() != null) { diary.setTitle(dto.title()); }
            if (dto.content() != null) { diary.setContent(dto.content()); }
            //TODO : 해시태그 업데이트 구현
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }

    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}