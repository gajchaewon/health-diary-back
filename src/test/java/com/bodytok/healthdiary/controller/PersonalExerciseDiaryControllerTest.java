package com.bodytok.healthdiary.controller;

import com.bodytok.healthdiary.config.TestSecurityConfig;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import com.bodytok.healthdiary.service.jwt.JwtService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("컨트롤러 - 다이어리")
@Import({TestSecurityConfig.class})
@WebMvcTest(PersonalExerciseDiaryController.class)
class PersonalExerciseDiaryControllerTest {

    private final MockMvc mvc;

    @MockBean
    private PersonalExerciseDiaryService diaryService;
    @MockBean
    private JwtService jwtService;

    public PersonalExerciseDiaryControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[GET] 다이어리 - 없는 데이터 호출")
    @Test
    public void givenDiaryId_whenRequestingADiary_thenReturnsNotFoundException() throws Exception {
        //Given
        Long diaryId = 999L;
        //When&Then

        mvc.perform(MockMvcRequestBuilders.get("/diary/{id}", diaryId))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


    @Disabled("구현 중")
    @DisplayName("[GET] 다이어리 리스트- 정상호출")
    @Test
    public void givenNothing_whenRequestingAllDiaries_thenReturnsJson() throws Exception {
        //Given

        //When
        when(diaryService.getAllDiaries(any(Pageable.class)));

        //Then
        mvc.perform(get("/diaries"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Disabled("구현 중")
    @DisplayName("[GET] 다이어리 상세 데이터 - 정상호출")
    @Test
    public void givenDiaryId_whenRequestingADiary_thenReturnsJson() throws Exception {
        //Given
        Long diaryId = 1L;
        //When&Then
        mvc.perform(get("/diaries/{diaryId}", diaryId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Disabled("구현 중")
    @DisplayName("[POST] 새 다이어리 작성- 정상호출")
    @Test
    public void givenInputData_whenCreatingANewDiary_thenReturnsJson() throws Exception {
        //Given

        //When&Then
        mvc.perform(post("/diaries"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Disabled("구현 중")
    @DisplayName("[PUT] 다이어리 리스트- 정상호출")
    @Test
    public void givenInputData_whenUpdatingADiary_thenReturnsUpdatedDiaryWithJson() throws Exception {
        //Given

        //When&Then
        mvc.perform(put("/diaries"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Disabled("구현 중")
    @DisplayName("[DELETE] 다이어리 리스트- 정상호출")
    @Test
    public void givenDiaryId_whenDeletingADiary_thenReturnsNothing() throws Exception {
        //Given
        Long diaryId = 1L;
        //When&Then
        mvc.perform(delete("/diaries", diaryId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }




    private PersonalExerciseDiaryDto createDiaryDto() {
        return PersonalExerciseDiaryDto.of(
                createUserAccountDto(),
                "title",
                "content",
                false,
                "testurl"
        );
    }



    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "test@email.com",
                "testuser",
                "testpwd",
                null
        );
    }

}
