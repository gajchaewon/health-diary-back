package com.bodytok.healthdiary.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@DisplayName("컨트롤러 - 다이어리")
@WebMvcTest(PersonalExerciseDiaryController.class)
class PersonalExerciseDiaryControllerTest {

    private final MockMvc mvc;

    public PersonalExerciseDiaryControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @Disabled("구현 중")
    @DisplayName("[GET] 다이어리 리스트 - 없는 데이터 호출")
    @Test
    public void givenDiaryId_whenRequestingADiary_thenReturnsNotFoundException() throws Exception {
        //Given
        Long diaryId = 999L;
        //When&Then

        mvc.perform(MockMvcRequestBuilders.get("/diary/{id}", diaryId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Disabled("구현 중")
    @DisplayName("[GET] 다이어리 리스트- 정상호출")
    @Test
    public void givenNothing_whenRequestingAllDiaries_thenReturnsJson() throws Exception {
        //Given

        //When&Then
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

}