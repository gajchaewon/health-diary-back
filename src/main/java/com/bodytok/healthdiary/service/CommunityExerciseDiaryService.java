package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.constant.SearchType;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bodytok.healthdiary.exepction.CustomError.*;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommunityExerciseDiaryService {

    private final PersonalExerciseDiaryRepository diaryRepository;


    //커뮤니티 다이어리 가져오기 - 댓글 미포함
    @Transactional(readOnly = true)
    public Page<DiaryDto> getAllDiaries(SearchType searchType, String keyword, Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {
            Page<PersonalExerciseDiary> diaries = diaryRepository.findAllByIsPublicTrue(pageable);
            return diaries.map(DiaryDto::from);
        }

        return switch (searchType){
            case TITLE -> diaryRepository.findByTitleContaining(null,keyword,pageable).map(DiaryDto::from);
            case CONTENT -> diaryRepository.findByContentContaining(null,keyword, pageable).map(DiaryDto::from);
            case HASHTAG -> diaryRepository.findByDiaryHashtag(null,keyword, pageable).map(DiaryDto::from);
            case NICKNAME -> diaryRepository.findByUserAccount_NicknameContainingAndIsPublicTrue(keyword, pageable).map(DiaryDto::from);
            case DATE, MONTH -> throw new CustomBaseException(DATE_SEARCH_UNSUPPORTED);
        };
    }


    //유저가 작성한 모든 다이어리 조회
    @Transactional(readOnly = true)
    public Page<DiaryWithCommentDto> getUserDiariesWithCommentsByUserId(Long userId, Pageable pageable) {
        return diaryRepository.findByUserAccount_IdAndIsPublicTrue(userId, pageable)
                .orElseThrow(() -> new CustomBaseException(DIARY_NOT_FOUND))
                .map(DiaryWithCommentDto::from);
    }




}

