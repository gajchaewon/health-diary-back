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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PersonalExerciseDiaryService {

    private final PersonalExerciseDiaryRepository diaryRepository;
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
        var diary = diaryRepository.findById(diaryId).orElseThrow(() -> new EntityNotFoundException("다이어리가 없습니다. - diaryId : " + diaryId));
        return DiaryDto.from(diary);
    }


    //다이어리 조회 - 댓글 포함
    @Transactional(readOnly = true)
    public DiaryWithCommentDto getDiaryWithComments(Long diaryId) {
        var diary = diaryRepository.findById(diaryId).orElseThrow(() -> new EntityNotFoundException("다이어리가 없습니다. - diaryId : " + diaryId));
        return DiaryWithCommentDto.from(diary);
    }

    //다이어리 조회 - 유저 인증 기반
    @Transactional(readOnly = true)
    public Page<DiaryWithCommentDto> getDiaryWithCommentsByUserId(Long userId, Pageable pageable){
        var diaries = diaryRepository.findByUserAccount_Id(userId, pageable);
        return diaries.map(DiaryWithCommentDto::from);

    }

    @Transactional(readOnly = true)
    public Page<DiaryWithCommentDto> getDiaryWithCommentsADay(String date, Pageable pageable) {
        // "yyyy-mm-dd" 형식의 문자열을 LocalDateTime으로 변환
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        log.info("변환된 localDate : {}", localDate);

        // 클라이언트에서 전달된 날짜의 최소 시각과 최대 시각 계산
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);

        log.info("startDateTime : {}\n", startDateTime);
        log.info("endDateTime : {}\n", endDateTime);

        Page<PersonalExerciseDiary> diaries = diaryRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);
        //빈 배열인지 확인
        if (diaries == null || diaries.getContent().isEmpty()) {
            // 빈 리스트에 대한 예외 처리 또는 다른 작업 수행
            throw new EntityNotFoundException("해당 날짜의 다이어리가 없습니다. date: " + date);
        }

        return diaries.map(DiaryWithCommentDto::from);
    }

    //다이어리 저장
    public DiaryDto saveDiaryWithHashtags(DiaryDto dto, Set<HashtagDto> hashtagDtoSet) {

        try{
            // Dto -> Entity
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());
            PersonalExerciseDiary diary = dto.toEntity(userAccount);

            diary = diaryRepository.save(diary);
            if (! hashtagDtoSet.isEmpty()){
                // 다이어리에 해시태그 추가
                for (HashtagDto hashtagDto : hashtagDtoSet) {
                    Hashtag hashtag = hashtagDto.toEntity();
                    diary.addHashtag(hashtag);
                }

            }

            // 커뮤니티에 저장하기
            if (diary.getIsPublic()) {
                communityExerciseDiaryService.savePublicDiary(diary);
            }
            return DiaryDto.from(diary);

        }catch (EntityNotFoundException e){
            log.warn("다이어리 저장 실패. 사용자 계정을 찾을 수 없습니다. - dto: {}", dto);
            throw e;
        }
    }

    //다이어리 수정
    public void updateDiary(Long diaryId, DiaryDto dto, Set<HashtagDto> hashtagDtoSet) {
        try {
            PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                    .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. - diaryId: " + diaryId));

            // 업데이트할 필드가 존재하는 경우에만 업데이트 진행
            if (dto.title() != null) {
                diary.setTitle(dto.title());
            }
            if (dto.content() != null) {
                diary.setContent(dto.content());
            }
            if (!hashtagDtoSet.isEmpty()){
                // 해시태그 업데이트
                updateHashtags(diary, hashtagDtoSet);
            }
            diaryRepository.save(diary);
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }

    // 해시태그 업데이트 관련 함수
    private void updateHashtags(PersonalExerciseDiary diary, Set<HashtagDto> hashtagDtoSet) {
        Set<Hashtag> newHashtags = hashtagDtoSet.stream()
                .map(HashtagDto::toEntity)
                .collect(Collectors.toSet());

        // 기존 해시태그 중 새로운 해시태그에 포함되지 않는 해시태그를 제거
        diary.getDiaryHashtags().removeIf(diaryHashtag -> !newHashtags.contains(diaryHashtag.getHashtag()));

        // 새로운 해시태그 추가
        for (Hashtag hashtag : newHashtags) {
            if (diary.getDiaryHashtags().stream().noneMatch(diaryHashtag -> diaryHashtag.getHashtag().equals(hashtag))) {
                PersonalExerciseDiaryHashtag diaryHashtag = PersonalExerciseDiaryHashtag.of(diary, hashtag);
                diary.getDiaryHashtags().add(diaryHashtag);
            }
        }
    }

    public void deleteDiary(Long diaryId) {
        try {
            PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                    .orElseThrow(() -> new EntityNotFoundException("다이어리를 찾을 수 없습니다. - diaryId: " + diaryId));

            //다이어리 연관 해시태그 삭제 -> entity bulk 삭제를 지원함
            diaryHashtagRepository.deleteAll(diary.getDiaryHashtags());

            //다이어리와 연관 댓글 모두 제거
            diary.getComments().clear();

            // 다이어리 삭제
            diaryRepository.delete(diary);
        } catch (EntityNotFoundException e) {
            log.warn("다이어리 삭제 실패. 다이어리를 찾을 수 없습니다. - diaryId: {}", diaryId);
        }
    }



}