package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.*;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PersonalExerciseDiaryService {

    private final PersonalExerciseDiaryRepository diaryRepository;
    private final HashtagRepository hashtagRepository;
    private final PersonalExerciseDiaryHashtagRepository diaryHashtagRepository;
    private final UserAccountRepository userAccountRepository;
    private final CommunityExerciseDiaryService communityExerciseDiaryService;


    //모든 다이어리 가져오기 - 댓글 미포함
    @Transactional(readOnly = true)
    public Page<DiaryDto> getAllDiaries(Pageable pageable) {
        Page<PersonalExerciseDiary> diaries = diaryRepository.findAll(pageable);

        return diaries.map(DiaryDto::from);
    }

    //다이어리 조회 - 댓글 미포함
    @Transactional(readOnly = true)
    public DiaryDto getDiary(Long diaryId) {
        var diary = diaryRepository.findById(diaryId).orElseThrow(()-> new EntityNotFoundException("다이어리가 없습니다. - diaryId : "+ diaryId));
        return DiaryDto.from(diary);
    }


    //다이어리 조회 - 댓글 포함
    @Transactional(readOnly = true)
    public DiaryWithCommentDto getDiaryWithComments(Long diaryId) {
        var diary = diaryRepository.findById(diaryId).orElseThrow(() -> new EntityNotFoundException("다이어리가 없습니다. - diaryId : "+ diaryId));
        return DiaryWithCommentDto.from(diary);
    }

    public DiaryDto saveDiaryWithHashtags(DiaryDto dto, Set<HashtagDto> hashtagDtoSet) {
        // Convert DTOs to entities
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());
        PersonalExerciseDiary diary = dto.toEntity(userAccount);
        Set<Hashtag> hashtags = hashtagDtoSet.stream()
                .map(HashtagDto::toEntity)
                .collect(Collectors.toUnmodifiableSet());
        // Save diary and hashtags
        diary = diaryRepository.save(diary);
        hashtags = hashtags.stream().map(hashtagRepository::save).collect(Collectors.toUnmodifiableSet());

        // Create and save DiaryHashtag entities
        for (Hashtag hashtag : hashtags) {
            var diaryHashtag = PersonalExerciseDiaryHashtag.of(
                    diary,
                    hashtag
            );
            diaryHashtagRepository.save(diaryHashtag);
        }

        // 커뮤니티에 저장하기
        if(diary.getIsPublic()){
            communityExerciseDiaryService.savePublicDiary(diary);
        }

        return DiaryDto.from(diary);
    }

    public void updateDiary(DiaryDto dto) {
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