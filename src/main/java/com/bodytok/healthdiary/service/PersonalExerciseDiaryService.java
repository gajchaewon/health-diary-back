package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.*;
import com.bodytok.healthdiary.domain.constant.SearchType;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diaryLike.LikeResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryHashtagRepository;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import com.bodytok.healthdiary.util.DateConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.bodytok.healthdiary.exepction.CustomError.*;

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
    private final LikeService likeService;


    //다이어리 조회 - 댓글 포함, private 은 주인만 조회 가능
    @Transactional(readOnly = true)
    public DiaryWithCommentDto getDiaryWithComments(Long diaryId, Long userId) {
        var diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomBaseException(DIARY_NOT_FOUND));
        if (!diary.getIsPublic()) {
            if (userId == null) {
                throw new CustomBaseException(DIARY_PRIVATE);
            } else if (!userId.equals(diary.getUserAccount().getId())) {
                throw new CustomBaseException(DIARY_PRIVATE);
            }
        }
        return DiaryWithCommentDto.from(diary);

    }

    //내 모든 다이어리 조회 - 인증 기반
    @Transactional(readOnly = true)
    public Page<DiaryWithCommentDto> getMyDiariesWithComments(
            Long userId, SearchType searchType, String keyword, Pageable pageable
    ) {

        if (keyword == null || keyword.isBlank()) {
            Page<PersonalExerciseDiary> diaries = diaryRepository.findByUserAccount_Id(userId, pageable);
            return diaries.map(DiaryWithCommentDto::from);
        }

        return switch (searchType) {
            case TITLE ->
                    diaryRepository.findByTitleContaining(userId, keyword, pageable).map(DiaryWithCommentDto::from);
            case CONTENT ->
                    diaryRepository.findByContentContaining(userId, keyword, pageable).map(DiaryWithCommentDto::from);
            case HASHTAG ->
                    diaryRepository.findByDiaryHashtag(userId, keyword, pageable).map(DiaryWithCommentDto::from);
            case DATE -> getMyDiariesByCreatedAt(userId, keyword, pageable);
            case MONTH -> getMyDiariesByMonthly(userId, keyword, pageable);
            case NICKNAME -> throw new CustomBaseException(NICKNAME_SEARCH_UNSUPPORTED);
        };
    }


    private Page<DiaryWithCommentDto> getMyDiaries(Long userId, String date, Pageable pageable, boolean isMonthly) {
        LocalDateTime[] timeRange = DateConverter.convertToTimeRange(date, isMonthly);
        LocalDateTime startTime = timeRange[0];
        LocalDateTime endTime = timeRange[1];
        var diaries = diaryRepository.findByUserAccount_IdAndCreatedAtBetween(userId, startTime, endTime, pageable);
        return diaries.map(DiaryWithCommentDto::from);
    }

    public Page<DiaryWithCommentDto> getMyDiariesByCreatedAt(Long userId, String date, Pageable pageable) {
        return getMyDiaries(userId, date, pageable, false);
    }

    public Page<DiaryWithCommentDto> getMyDiariesByMonthly(Long userId, String date, Pageable pageable) {
        return getMyDiaries(userId, date, pageable, true);
    }



    //다이어리 저장
    public DiaryDto saveDiaryWithHashtagsAndImages(DiaryDto dto, Set<Long> imageIds) {
        // Dto -> Entity
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());
        PersonalExerciseDiary diary = dto.toEntity(userAccount);
        Set<DiaryImage> diaryImageSet = imageService.searchImages(imageIds);

        diary = diaryRepository.save(diary);

        //새 해시태그만 생성
        if (!dto.hashtagDtoSet().isEmpty()) {
            Set<Hashtag> hashtags = hashtagService.renewHashtagsFromRequest(dto.hashtagDtoSet());
            for (Hashtag hashtag : hashtags) {
                diary.addHashtag(hashtag);
            }
        }

        //이미지 연결
        if (!diaryImageSet.isEmpty()) {
            for (DiaryImage diaryImage : diaryImageSet) {
                diary.addDiaryImage(diaryImage);
            }
        }
        return DiaryDto.from(diary);
    }

    //다이어리 수정
    public void updateDiary(DiaryDto dto, Set<Long> imageIds) {
        PersonalExerciseDiary diary = diaryRepository.findById(dto.id())
                .orElseThrow(() -> new CustomBaseException(DIARY_NOT_FOUND));
        if (!dto.userAccountDto().id().equals(diary.getUserAccount().getId())) {
            throw new CustomBaseException(DIARY_NOT_OWNER);
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
        PersonalExerciseDiary diaryWithUpdatedHashtags = updateHashtags(diary, dto.hashtagDtoSet());
        //image 정보 업데이트
        PersonalExerciseDiary updatedDiary = imageService.updateImages(diaryWithUpdatedHashtags, imageIds);

        // 해시태그, 이미지가 업데이트된 다이어리 저장.
        diaryRepository.save(updatedDiary);
    }

    //새로운 해시태그는 저장하고 있던 것은 제거하는 메서드
    public PersonalExerciseDiary updateHashtags(PersonalExerciseDiary diary, Set<HashtagDto> hashtagDtoSet) {
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
        } else {
            //업데이트된 해시태그 셋이 비어있으면 모두 제거
            diary.getDiaryHashtags().clear();
        }
        return diary;
    }


    public void deleteDiary(Long diaryId) {
        PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomBaseException(DIARY_NOT_FOUND));
        //다이어리 연관 해시태그 삭제 -> entity bulk 삭제를 지원함
        diaryHashtagRepository.deleteAll(diary.getDiaryHashtags());
        var images = diary.getDiaryImages();

        //다이어리와 연관된 이미지 삭제
        images.forEach(image -> imageService.deleteImage(image.getId()));

        //다이어리와 연관 댓글 모두 제거
        diary.getComments().clear();

        // 다이어리 삭제
        diaryRepository.delete(diary);
    }

    //TODO : 좋아요 서비스 클래스 쪽으로 옮기기
    public LikeResponse likeDiary(Long diaryId, Long userId) {
        PersonalExerciseDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomBaseException(DIARY_NOT_FOUND));
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new CustomBaseException(USER_NOT_FOUND));
        // diary가 공개되지 않으면 like 불가
        if (!diary.getIsPublic()) {
            throw new CustomBaseException(DIARY_PRIVATE);
        }
        // 다이어리의 like set 을 읽어 유저가 저장돼 있는지(눌렀던 것인지) 확인
        Optional<DiaryLike> diaryLike = diary.getLikes().stream()
                .filter(like -> like.getUserAccount().equals(userAccount)).findFirst();
        if (diaryLike.isPresent()) {
            //좋아요 취소
            diary.removeLike(diaryLike.get());
        } else {
            // 좋아요 생성
            diary.addLike(DiaryLike.of(userAccount, diary));
        }
        diaryRepository.save(diary);

        return LikeResponse.of(diary.getLikes().size());
    }

    public Page<DiaryWithCommentDto> getDiariesByUserLiked(Long userId, Pageable pageable) {
        Page<DiaryLike> myLikes = likeService.getLikesByUserId(userId, pageable);
        List<DiaryWithCommentDto> dtoList = myLikes.getContent().stream()
                .map(like -> DiaryWithCommentDto.from(like.getPersonalExerciseDiary()))
                .toList();
        return new PageImpl<>(dtoList, pageable, myLikes.getTotalElements());
    }

    public Integer getDiaryCount(Long userId) {
        return diaryRepository.countByUserAccount_Id(userId);
    }

}