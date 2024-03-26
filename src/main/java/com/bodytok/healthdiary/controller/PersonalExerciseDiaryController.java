package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.auth.response.RegisterResponse;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diary.request.DiaryRequest;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.exepction.CommonApiError;
import com.bodytok.healthdiary.exepction.ValidationError;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
@Tag(name = "Diary")
public class PersonalExerciseDiaryController {

    private final PersonalExerciseDiaryService diaryService;

    @GetMapping
    @Operation(summary = "모든 다이어리 가져오기 - 댓글 미포함")
    @SecurityRequirements(value = {}) // Swagger 글로벌 security 설정 지우기
    public ResponseEntity<Page<DiaryResponse>> getAllDiaries(
           @ParameterObject @PageableDefault(sort = "createdAt") Pageable pageable
    ) {
        Page<DiaryDto> diaries = diaryService.getAllDiaries(pageable);

        return ResponseEntity.ok().body(
                diaries.map(DiaryResponse::from)
        );
    }


    @PostMapping
    @Operation(summary = "새로운 다이어리 생성하기")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryResponse.class))
    })
    public ResponseEntity<DiaryResponse> createDiary(
            @RequestBody DiaryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        // 해시태그가 비어 있는 경우에 대비하여 빈 Set으로 전달
        Set<HashtagDto> hashtags = request.hashtags() != null ?
                request.hashtags().stream().map(HashtagDto::of).collect(Collectors.toUnmodifiableSet()) :
                Collections.emptySet();

        DiaryDto diary = diaryService.saveDiaryWithHashtags(
                request.toDto(userDetails.toDto()),
                hashtags
        );
        DiaryResponse diaryResponse = DiaryResponse.from(diary);

        return ResponseEntity.ok(diaryResponse);
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "다이어리 조회 - 댓글 포함")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryWithCommentResponse.class))
    })
    @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<DiaryWithCommentResponse> getDiaryWithComments(
            @PathVariable(name = "diaryId") Long diaryId
    ) {
        DiaryWithCommentDto diary = diaryService.getDiaryWithComments(diaryId);

        return ResponseEntity.ok(DiaryWithCommentResponse.from(diary));
    }

    @GetMapping("/search")
    @Operation(summary = "다이어리 조회 - 날짜 검색")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryWithCommentResponse.class))
    })
    @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<Page<DiaryWithCommentResponse>> getDiaryWithCommentsADay(
            @Parameter(name = "date",description = "원하는 날짜(yyyy-MM-dd) 를 String 형식으로 기입")
            @RequestParam(name = "date") String date,
            @ParameterObject @PageableDefault(sort = "createdAt") Pageable pageable
            ){
        Page<DiaryWithCommentDto> diaries = diaryService.getDiaryWithCommentsADay(date, pageable);
        return ResponseEntity.ok().body(
                diaries.map(DiaryWithCommentResponse::from)
        );
    }

    @GetMapping("/my")
    @Operation(summary = "다이어리 조회 - 유저 인증 기반")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryWithCommentResponse.class))
    })
    public ResponseEntity<Page<DiaryWithCommentResponse>> getDiariesWithCommentsByUserId(
            @ParameterObject @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        var diaries = diaryService.getDiaryWithCommentsByUserId(userDetails.getId(), pageable);
        return ResponseEntity.ok().body(
                diaries.map(DiaryWithCommentResponse::from)
        );
    }

    @PutMapping("/{diaryId}")
    @Operation(summary = "다이어리 수정")
    public ResponseEntity<Void> updateDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @RequestBody DiaryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 해시태그가 비어 있는 경우에 대비하여 빈 Set으로 전달
        Set<HashtagDto> hashtags = request.hashtags() != null ?
                request.hashtags().stream().map(HashtagDto::of).collect(Collectors.toSet()) :
                Collections.emptySet();

        diaryService.updateDiary(
                diaryId,
                request.toDto(userDetails.toDto()),
                hashtags
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "다이어리 삭제")
    public ResponseEntity<Void> deleteDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        diaryService.deleteDiary(diaryId, userDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{diaryId}/like")
    @Operation(summary = "다이어리 좋아요")
    public ResponseEntity<Void> likeDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        diaryService.likeDiary(diaryId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

}
