package com.bodytok.healthdiary.controller;

import com.bodytok.healthdiary.config.TestSecurityConfig;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diary.request.DiaryRequest;
import com.bodytok.healthdiary.dto.diaryLike.DiaryLikeInfo;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bodytok.healthdiary.config.SecurityTestUtil.setUpMockUser;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@DisplayName("컨트롤러 - 다이어리")
@Import({TestSecurityConfig.class})
@WebMvcTest(PersonalExerciseDiaryController.class)
class PersonalExerciseDiaryControllerTest {


    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private PersonalExerciseDiaryService diaryService;

    public PersonalExerciseDiaryControllerTest(@Autowired MockMvc mvc,
                                               @Autowired ObjectMapper objectMapper

    ) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }


    @DisplayName("[GET] 다이어리 - 없는 데이터 호출")
    @Test
    public void givenDiaryId_whenRequestingADiary_thenReturnsNotFoundException() throws Exception {
        //Given
        Long diaryId = 99999L;
        //When&Then
        mvc.perform(get("/diary/{id}", diaryId))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


    @DisplayName("[GET] 내 모든 다이어리 리스트 조회- 정상호출")
    @Test
    public void givenNothing_whenRequestingAllMyDiaries_thenReturnsJson() throws Exception {
        //Given
        Pageable pageable = PageRequest.of(0, 1); // 1개만 가져오기
        var userDetails = setUpMockUser();
        var dtoPage = createDiaryWithCommentDtoPage(1, pageable); //1개만 생성

        //When
        given(diaryService.getMyDiariesWithComments(eq(userDetails.getId()), isNull(), isNull(), any(Pageable.class)))
                .willReturn(dtoPage);

        //Then
        mvc.perform(get("/diaries/my")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                )
                .andExpect(status().isOk())
                .andDo(print());

        then(diaryService).should().getMyDiariesWithComments(eq(userDetails.getId()), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("[GET] 다이어리 상세 데이터 - 정상호출")
    public void givenDiaryId_whenRequestingADiary_thenReturnsJson() throws Exception {
        //Given
        var userDetails = setUpMockUser();
        Long diaryId = 1L;
        var diaryWithCommentDto = createDiaryWithCommentDto();
        given(diaryService.getDiaryWithComments(eq(diaryId), eq(userDetails.getId()))).willReturn(diaryWithCommentDto);

        //When&Then
        mvc.perform(get("/diaries/{diaryId}", diaryId))
                .andExpect(status().isOk())
                .andDo(print());

        then(diaryService).should()
                .getDiaryWithComments(eq(diaryId), eq(userDetails.getId()));

    }



    @DisplayName("[POST] 새 다이어리 작성- 정상호출")
    @Test
    public void givenInputData_whenCreatingANewDiary_thenReturnsJson() throws Exception {
        //Given
        var userDetails = setUpMockUser();

        Set<String> hashtags = Set.of("테스트 코드 어려워", "왜 되지?");
        DiaryRequest request = DiaryRequest.of("title", "content", true, hashtags, Set.of());
        Set<HashtagDto> hashtagDtoSet = mapHashtagsStringSet(hashtags);

        var requestDto = request.toDto(userDetails.toDto());
        var responseDto = createReturnDiaryDto(userDetails);

        given(diaryService.saveDiaryWithHashtags(eq(requestDto), eq(hashtagDtoSet), eq(Set.of()))).willReturn(responseDto);

        var json = objectMapper.writeValueAsString(request);

        // When & Then
        mvc.perform(post("/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        then(diaryService).should().saveDiaryWithHashtags(eq(requestDto), eq(hashtagDtoSet), eq(Set.of()));

    }



    private DiaryDto createReturnDiaryDto(CustomUserDetails userDetails) {
        return new DiaryDto(
                1L,
                userDetails.toDto(),
                "title",
                "content",
                0,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Set.of(HashtagDto.of(1L,"테스트 코드 어려워" ), HashtagDto.of(2L,"왜 되지?")),
                null,
                List.of()

        );
    }

    private DiaryWithCommentDto createDiaryWithCommentDto() {
        var user = createUserAccountDto();
        return new DiaryWithCommentDto(1L, user, Set.of(), "title", "content",
                0, true, LocalDateTime.now(), LocalDateTime.now(),
                Set.of(HashtagDto.of("test")), new DiaryLikeInfo(Set.of(), 0), List.of());
    }

    private Page<DiaryWithCommentDto> createDiaryWithCommentDtoPage(int count, Pageable pageable) {
        List<DiaryWithCommentDto> diaryList = IntStream.range(0, count)
                .mapToObj(i -> createDiaryWithCommentDto())
                .collect(Collectors.toList());
        return new PageImpl<>(diaryList, pageable, count);
    }


    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "210@mail.com",
                "이원영",
                "password1234!",
                null
        );
    }

    private static Set<HashtagDto> mapHashtagsStringSet(Set<String> hashtags) {
        return hashtags.stream()
                .map(hashtag -> new HashtagDto(null, hashtag))
                .collect(Collectors.toSet());
    }

}
