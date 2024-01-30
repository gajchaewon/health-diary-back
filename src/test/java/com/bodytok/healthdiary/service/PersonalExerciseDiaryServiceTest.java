package com.bodytok.healthdiary.service;

import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@DisplayName("비즈니스 로직 - 다이어리")
@ExtendWith(MockitoExtension.class)
class PersonalExerciseDiaryServiceTest {

    @InjectMocks private PersonalExerciseDiaryService sut;

    @Mock private PersonalExerciseDiaryRepository diaryRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("모든 다이어리를 조회하면, 반환한다.")
    @Test
    void givenNothing_whenRequestingDiaries_thenReturnsThemAll() {
        //Given
        Pageable pageable = Pageable.ofSize(20);
        given(diaryRepository.findAll(pageable)).willReturn(Page.empty());
        //When
        Page<PersonalExerciseDiaryDto> diaries = sut.getAllDiaries(pageable);
        //Then

        assertThat(diaries).isNotNull();
        then(diaryRepository).should().findAll(pageable);
    }

    @DisplayName("다이어리를 ID로 조회하면, 반환한다.")
    @Test
    void givenDiaryId_whenRequestingADiary_thenReturnsIt() {
        //Given
        Long diaryId = 1L;
        PersonalExerciseDiary diary = createPersonalExerciseDiary();
        given(diaryRepository.findById(diaryId)).willReturn(Optional.of(diary));
        //When
        PersonalExerciseDiaryDto dto = sut.getDiary(diaryId);
        //Then

        then(diaryRepository).should().findById(diaryId);
    }

    @DisplayName("없는 다이어리를 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentDiaryId_whenSearchingDiary_thenThrowsException() {
        // Given
        Long diaryId = 0L;
        given(diaryRepository.findById(diaryId)).willReturn(Optional.empty());
        // When
        Throwable t = catchThrowable(() -> sut.getDiary(diaryId));
        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("다이어리가 없습니다. - diaryId : " + diaryId);
        then(diaryRepository).should().findById(diaryId);
    }

    @DisplayName("다이어리 정보를 입력하면, 저장한다.")
    @Test
    void givenDiaryInfo_whenCreatingANewDiary_thenSavesIt() {
        PersonalExerciseDiaryDto dto = createDiaryDto();
        given(diaryRepository.save(any(PersonalExerciseDiary.class))).willReturn(createPersonalExerciseDiary());
        // When
        sut.saveDiary(dto);
        // Then
        then(diaryRepository).should().save(any(PersonalExerciseDiary.class));
    }



    @DisplayName("다이어리 ID와 수정 정보를 입력하면, 게시글을 수정한다")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        PersonalExerciseDiary diary = createPersonalExerciseDiary();
        PersonalExerciseDiaryDto dto = createDiaryDto("새 타이틀", "새 내용", false,"");
        given(diaryRepository.getReferenceById(dto.id())).willReturn(diary);
        // When
        sut.updateDiary(dto);
        // Then
        assertThat(diary)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content());
        then(diaryRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long diaryId = 1L;
        willDoNothing().given(diaryRepository).deleteById(diaryId);
        // When
        sut.deleteDiary(1L);
        // Then
        then(diaryRepository).should().deleteById(diaryId);
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
                "password",
                null
        );
    }

    private PersonalExerciseDiary createPersonalExerciseDiary() {
        PersonalExerciseDiary diary = PersonalExerciseDiary.of(
                createUserAccount(),
                "title",
                "content",
                0,
                false,
                "testurl"
        );
        //TODO : ReflectionTestUtils 학습
        ReflectionTestUtils.setField(diary, "id", 1L);

        return diary;
    }

    private PersonalExerciseDiaryDto createDiaryDto() {
        return createDiaryDto("title", "content", false, "testurl");
    }

    private PersonalExerciseDiaryDto createDiaryDto(String title, String content, Boolean isPublic, String youtubeUrl) {
        return PersonalExerciseDiaryDto.of(
                createUserAccountDto(),
                title,
                content,
                isPublic,
                youtubeUrl
        );
    }


}
