package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.*;
import com.bodytok.healthdiary.domain.constant.SearchType;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diaryLike.LikeResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.repository.*;
import com.bodytok.healthdiary.util.DateConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PersonalExerciseDiaryService {

    private final PersonalExerciseDiaryRepository diaryRepository;
    private final PersonalExerciseDiaryHashtagRepository diaryHashtagRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagService hashtagService;
    private final ImageService imageService;


    //다이어리 조회 - 댓글 포함
    @Transactional(readOnly = true)
    public DiaryWithCommentDto getDiaryWithComments(Long diaryId, CustomUserDetails userDetails) {
        var diary = diaryRepository.findById(diaryId).orElseThrow(() -> new EntityNotFoundException("다이어리가 없습니다. - diaryId : " + diaryId));
        Long userId = userDetails.getId();

        //다이어리 소유자라면 공개 여부 상관없이 리턴
        if (diary.getUserAccount().getId().equals(userId)){
            return DiaryWithCommentDto.from(diary);
        }
        //다이어리 소유자가 아니라면 -> 공개된 다이어리만 리턴 가능
        if (diary.getIsPublic()){
            return DiaryWithCommentDto.from(diary);
        } else {
            throw new AccessDeniedException("공개된 다이어리가 아닙니다. - isPublic : false");
        }
    }

    //내 모든 다이어리 조회 - 인증 기반
    @Transactional(readOnly = true)
    public Page<DiaryWithCommentDto> getMyDiariesWithCommentsByUserId(
            CustomUserDetails userDetails, SearchType searchType, String keyword, Pageable pageable
    ) {
        var userId = userDetails.getId();

        if (keyword == null || keyword.isBlank()) {
            Page<PersonalExerciseDiary> diaries = diaryRepository.findByUserAccount_Id(userId, pageable);
            return diaries.map(DiaryWithCommentDto::from);
        }

        return switch (searchType){
            case TITLE -> diaryRepository.findByTitleContaining(userId,keyword,pageable).map(DiaryWithCommentDto::from);
            case CONTENT -> diaryRepository.findByContentContaining(userId,keyword, pageable).map(DiaryWithCommentDto::from);
            case HASHTAG -> diaryRepository.findByDiaryHashtag(userId,keyword, pageable).map(DiaryWithCommentDto::from);
            case DATE -> getMyDiariesByCreatedAt(userId,keyword,pageable);
            case NICKNAME -> throw new IllegalArgumentException("Nickname 검색은 지원되지 않습니다.");
        };
    }


    //내 모든 다이어리 조회 - 인증 기반
    private Page<DiaryWithCommentDto> getMyDiariesByCreatedAt(Long userId, String date, Pageable pageable) {
        //문자열을 받아 db검색에 맞게 변환
        LocalDateTime[] timeRange = DateConverter.convertDateToTimeRange(date);
        LocalDateTime startTime = timeRange[0];
        LocalDateTime endTime = timeRange[1];
        var diaries = diaryRepository.findByUserAccount_IdAndCreatedAtBetween(userId,startTime, endTime, pageable);
        return diaries.map(DiaryWithCommentDto::from);
    }


    //다이어리 저장
    public DiaryDto saveDiaryWithHashtags(DiaryDto dto, Set<HashtagDto> hashtagDtoSet, Set<Long> imageIds) {

        try {
            // Dto -> Entity
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());
            PersonalExerciseDiary diary = dto.toEntity(userAccount);
            Set<DiaryImage> diaryImageSet = imageService.getImages(imageIds);

            diary = diaryRepository.save(diary);

            if (!hashtagDtoSet.isEmpty()) {
                Set<Hashtag> hashtags = hashtagService.renewHashtagsFromRequest(hashtagDtoSet);
                for (Hashtag hashtag : hashtags) {
                    diary.addHashtag(hashtag);
                }
            }

            if(!diaryImageSet.isEmpty()){
                for (DiaryImage diaryImage : diaryImageSet){
                    diary.addDiaryImage(diaryImage);
                }
            }
            

            return DiaryDto.from(diary);

        } catch (DataAccessException e) {
            log.warn("다이어리 저장 실패. dto : {}\n error-message : {}", dto, e.getLocalizedMessage());
            throw e;
        }
    }

    //다이어리 수정
    public void updateDiary(Long diaryId, DiaryDto dto, Set<HashtagDto> hashtagDtoSet, Set<Long> imageIds) {
        try {
            PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                    .orElseThrow(() -> new EntityNotFoundException("다이어리를 찾을 수 없습니다. - diaryId: " + diaryId));
            if (!dto.userAccountDto().id().equals(diary.getUserAccount().getId())){
                throw new AccessDeniedException("다이어리 소유자가 아닙니다. - 다이어리의 userId : "+ diary.getUserAccount().getId());
            }
            // 업데이트할 필드가 존재 & 다를 경우에만 필드업데이트 진행
            if (dto.title() != null && !dto.title().equals(diary.getTitle())) {
                diary.setTitle(dto.title());
            }
            if (dto.content() != null && !dto.content().equals(diary.getContent())) {
                diary.setContent(dto.content());
            }
            if (dto.isPublic() != diary.getIsPublic()) {
                diary.setIsPublic(dto.isPublic());
            }

            // 해시태그 업데이트
            PersonalExerciseDiary diaryWithUpdatedHashtags = updateHashtags(diary, hashtagDtoSet);
            PersonalExerciseDiary updatedDiary = imageService.updateImages(diaryWithUpdatedHashtags, imageIds);

            // 해시태그, 이미지가 업데이트된 다이어리 저장.
            diaryRepository.save(updatedDiary);
        } catch (EntityNotFoundException e) {
            log.warn("다이어리 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
            throw new EntityNotFoundException("다이어리를 찾을 수 없습니다 - errorMessage : " + e.getMessage());
        }
    }

    //새로운 해시태그는 저장하고 있던 것은 제거하는 메서드
    private PersonalExerciseDiary updateHashtags(PersonalExerciseDiary diary, Set<HashtagDto> hashtagDtoSet) {
        if (!hashtagDtoSet.isEmpty()) {
            //기존 해시태그 엔티티에 있는 해시태그는 그대로 사용
            Set<Hashtag> updatedHashtags = hashtagService.renewHashtagsFromRequest(hashtagDtoSet);

            //기존 해시태그 중 업데이트 해시태그셋에 포함되지 않는 해시태그를 제거
            diary.getDiaryHashtags().removeIf(diaryHashtag -> !updatedHashtags.contains(diaryHashtag.getHashtag()));

            // 새로운 해시태그를 추가
            for (Hashtag hashtag : updatedHashtags) {
                if (diary.getDiaryHashtags().stream().noneMatch(diaryHashtag -> diaryHashtag.getHashtag().equals(hashtag))) {
                    diary.addHashtag(hashtag);
                }
            }
            return diary;
        }else {
            //업데이트된 해시태그 셋이 비어있으면 모두 제거
            diary.getDiaryHashtags().clear();
        }
        return diary;
    }



    public void deleteDiary(Long diaryId, CustomUserDetails userDetails) {
        try {
            PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                    .orElseThrow(() -> new EntityNotFoundException("다이어리를 찾을 수 없습니다. - diaryId: " + diaryId));
            if(!diary.getUserAccount().getId().equals(userDetails.getId())){
                throw new AccessDeniedException("다이어리 소유자가 아닙니다. - 다이어리의 userId : "+ diary.getUserAccount().getId());
            }
            //다이어리 연관 해시태그 삭제 -> entity bulk 삭제를 지원함
            diaryHashtagRepository.deleteAll(diary.getDiaryHashtags());
            var images = diary.getDiaryImages();

            //다이어리와 연관된 이미지 삭제
            images.forEach(image -> imageService.deleteImage(image.getId()));

            //다이어리와 연관 댓글 모두 제거
            diary.getComments().clear();

            // 다이어리 삭제
            diaryRepository.delete(diary);
        } catch (EntityNotFoundException e) {
            log.warn("다이어리 삭제 실패. 다이어리를 찾을 수 없습니다. - diaryId: {}", diaryId);
            throw new EntityNotFoundException("다이어리를 찾을 수 없습니다 - errorMessage :  " + e.getMessage());
        }
    }

    public LikeResponse likeDiary(Long diaryId, Long userId) {
        try {
            PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                    .orElseThrow(() -> new EntityNotFoundException("다이어리를 찾을 수 없습니다. - diaryId: " + diaryId));
            UserAccount userAccount = userAccountRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. - userId: " + userId));

            // diary가 공개되지 않으면 like 불가
            if (!diary.getIsPublic()){
                throw new AccessDeniedException("공개되지 않은 다이어리는 좋아요가 불가합니다.");
            }

            // 다이어리의 like set 을 읽어 유저가 저장돼 있는지(눌렀던 것인지) 확인
            Optional<DiaryLike> diaryLike = diary.getLikes().stream()
                    .filter(like -> like.getUserAccount().equals(userAccount)).findFirst();

            if (diaryLike.isPresent()) {
                //좋아요 취소
                diary.removeLike(diaryLike.get());
            } else {
                // 좋아요 생성
                DiaryLike like = DiaryLike.of(userAccount, diary);
                diary.addLike(like);
            }
            diaryRepository.save(diary);

            return LikeResponse.of(diary.getLikes().size());
        } catch (EntityNotFoundException e) {
            log.warn("다이어리 좋아요 실패: 다이어리 또는 사용자를 찾을 수 없습니다. - diaryId: {}, userId: {}", diaryId, userId);
            throw new EntityNotFoundException("like 실패 : 엔티티가 없습니다.", e);
        }
    }


}