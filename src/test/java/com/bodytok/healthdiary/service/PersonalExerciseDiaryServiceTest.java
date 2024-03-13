package com.bodytok.healthdiary.service;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.PersonalExerciseDiaryHashtag;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryHashtagRepository;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@Disabled("개발 중")
@DisplayName("비즈니스 로직 - 다이어리")
@ExtendWith(MockitoExtension.class)
class PersonalExerciseDiaryServiceTest {

    @InjectMocks private PersonalExerciseDiaryService sut;

    @Mock private PersonalExerciseDiaryRepository diaryRepository;

    @Mock private PersonalExerciseDiaryHashtagRepository diaryHashtagRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("모든 다이어리 조회")
    @Test
    void givenNothing_whenRequestingDiaries_thenReturnsThemAll() {
        //Given
        Pageable pageable = Pageable.ofSize(20);
        given(diaryRepository.findAll(pageable)).willReturn(Page.empty());
        //When
        Page<DiaryDto> diaries = sut.getAllDiaries(pageable);
        //Then

        assertThat(diaries).isNotNull();
        then(diaryRepository).should().findAll(pageable);
    }

    @DisplayName("다이어리 ID로 조회")
    @Test
    void givenDiaryId_whenRequestingADiary_thenReturnsIt() {
        //Given
        Long diaryId = 1L;
        PersonalExerciseDiary diary = createPersonalExerciseDiary();
        given(diaryRepository.findById(diaryId)).willReturn(Optional.of(diary));
        //When
        DiaryDto dto = sut.getDiary(diaryId);
        //Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(diaryId);
        then(diaryRepository).should().findById(diaryId);
    }

    @DisplayName("다이어리 ID로 조회 - 존재하지 않는 경우 예외를 던진다.")
    @Test
    void givenNonexistentDiaryId_whenSearchingDiary_thenThrowsException() {
        // Given
        Long diaryId = 0L;
        given(diaryRepository.findById(diaryId)).willReturn(Optional.empty());
        // When
        Throwable t = catchThrowable(() -> sut.getDiary(diaryId));
        // Then
        assertThat(t).isInstanceOf(EntityNotFoundException.class);
        then(diaryRepository).should().findById(diaryId);
    }

    @DisplayName("다이어리 저장 - 모든 필드 입력")
    @Test
    void givenDiaryInfo_whenCreatingANewDiary_thenSavesIt() {
        DiaryDto dto = createDiaryDto();
        Set<HashtagDto> hashtagDtoSet = createHashtagDtoSet();

        given(diaryRepository.save(any(PersonalExerciseDiary.class))).willReturn(createPersonalExerciseDiary());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().id())).willReturn(createUserAccount());
        // When
        DiaryDto result = sut.saveDiaryWithHashtags(dto, hashtagDtoSet);
        // Then
        assertThat(result).isNotNull();
        then(diaryRepository).should().save(any(PersonalExerciseDiary.class));
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().id());
    }



    @DisplayName("다이어리 수정 - 제목 및 내용 수정")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        Long diaryId = 1L;
        DiaryDto dto = createDiaryDto("새 타이틀", "새 내용", false);
        Set<HashtagDto> hashtagDtoSet = createHashtagDtoSet();
        PersonalExerciseDiary diary = createPersonalExerciseDiary();
        // 다이어리에 해시태그 연결
        Set<PersonalExerciseDiaryHashtag> diaryHashtags = hashtagDtoSet.stream()
                .map(hashtagDto -> PersonalExerciseDiaryHashtag.of(diary, hashtagDto.toEntity()))
                .collect(Collectors.toSet());

        for (var diaryHashtag : diaryHashtags){
            diary.getDiaryHashtags().add(diaryHashtag);
        }
//        given(diaryRepository.getReferenceById(diaryId)).willReturn(any(PersonalExerciseDiary.class));

        // When
        sut.updateDiary(dto, hashtagDtoSet);

        // Then
        assertThat(diary.getTitle()).isEqualTo(dto.title());
        assertThat(diary.getContent()).isEqualTo(dto.content());
        assertThat(diary.getDiaryHashtags().size()).isEqualTo(hashtagDtoSet.size());
        for (HashtagDto hashtagDto : hashtagDtoSet) {
            assertThat(diary.getDiaryHashtags().stream().anyMatch(diaryHashtag ->
                    diaryHashtag.getHashtag().getHashtag().equals(hashtagDto.hashtag())
            )).isTrue();
        }
//        then(diaryRepository).should().getReferenceById(diaryId);
        then(diaryRepository).should().save(diary);
    }

    @DisplayName("다이어리 삭제 - 존재하는 다이어리의 경우")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long diaryId = 1L;
        PersonalExerciseDiary diary = createPersonalExerciseDiary();
        Set<HashtagDto> hashtagDtoSet = createHashtagDtoSet();
        // 다이어리에 해시태그 연결
        Set<PersonalExerciseDiaryHashtag> diaryHashtags = hashtagDtoSet.stream()
                .map(hashtagDto -> PersonalExerciseDiaryHashtag.of(diary, hashtagDto.toEntity()))
                .collect(Collectors.toSet());
        for (var diaryHashtag : diaryHashtags){
            diary.getDiaryHashtags().add(diaryHashtag);
        }

        given(diaryRepository.getReferenceById(diaryId)).willReturn(any(PersonalExerciseDiary.class));

        // When
        sut.deleteDiary(1L);

        // Then
        then(diaryRepository).should().getReferenceById(diaryId);
        then(diaryRepository).should().delete(diary);
        //Cascade.ALL
        then(diaryHashtagRepository).should().deleteAll(diaryHashtags);
    }


    private UserAccount createUserAccount() {
        return UserAccount.of(
                "twonezero@gmail.com",
                "twonezero",
                "asdf1234",
                null
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "twonezero@gmail.com",
                "twonezero",
                "password"
        );
    }


    private PersonalExerciseDiary createPersonalExerciseDiary() {
        PersonalExerciseDiary diary = PersonalExerciseDiary.of(
                createUserAccount(),
                "새 타이틀",
                "새 내용",
                0,
                false
        );
        //TODO : ReflectionTestUtils 학습
        ReflectionTestUtils.setField(diary, "id", 1L);

        return diary;
    }

    private Set<HashtagDto> createHashtagDtoSet() {
        Set<HashtagDto> hashtags = new HashSet<>();
        hashtags.add(HashtagDto.of("tag1"));
        hashtags.add(HashtagDto.of("tag2"));
        return hashtags;
    }

    private DiaryDto createDiaryDto() {
        return createDiaryDto("새 타이틀", "새 내용", false);
    }

    private DiaryDto createDiaryDto(String title, String content, Boolean isPublic) {
        return DiaryDto.of(
                createUserAccountDto(),
                title,
                content,
                isPublic
        );
    }


}
