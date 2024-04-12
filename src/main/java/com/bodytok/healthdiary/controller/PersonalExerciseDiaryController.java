package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.constant.SearchType;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diary.request.DiaryRequest;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.dto.diaryLike.LikeResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.exepction.CommonApiError;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
                hashtags,
                request.imageIds()
        );
        DiaryResponse diaryResponse = DiaryResponse.from(diary);

        return ResponseEntity.ok(diaryResponse);
    }

    @GetMapping("/my")
    @Operation(summary = "나의 모든 다이어리 조회 - 날짜검색(optional) | ")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryWithCommentResponse.class))
    })
    public ResponseEntity<Page<DiaryWithCommentResponse>> getDiariesWithCommentsByUserId(
            @Parameter(name = "searchType",description = "검색하고자 하는 필드")
            @RequestParam(required = false,name = "searchType") SearchType searchType,
            @Parameter(name = "searchValue",description = "검색 키워드 - 검색 타입에 맞게 검색")
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @ParameterObject @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ){
        var diaries = diaryService.getMyDiariesWithComments(userDetails,searchType, searchValue,pageable);
        return ResponseEntity.ok().body(
                diaries.map(DiaryWithCommentResponse::from)
        );
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

    @PutMapping("/{diaryId}")
    @Operation(summary = "다이어리 수정 - 이미지 먼저 저장 or 삭제 후 진행")
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
                hashtags,
                request.imageIds()

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
    public ResponseEntity<LikeResponse> likeDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LikeResponse response = diaryService.likeDiary(diaryId, userDetails.getId());
        return ResponseEntity.ok().body(response);
    }

}
