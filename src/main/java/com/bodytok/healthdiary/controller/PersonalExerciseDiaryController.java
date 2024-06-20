package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.constant.SearchType;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diary.request.DiaryCreate;
import com.bodytok.healthdiary.dto.diary.request.DiaryUpdate;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.dto.diaryLike.LikeResponse;
import com.bodytok.healthdiary.exepction.ApiErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
@Tag(name = "Diary")
public class PersonalExerciseDiaryController {

    private final PersonalExerciseDiaryService diaryService;
//    private final DiaryMapper diaryMapper = DiaryMapper.INSTANCE;

    @PostMapping
    @Operation(summary = "새로운 다이어리 생성하기")
    @ApiResponse(responseCode = "201", description = "Diary Created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryResponse.class))
    })
    public ResponseEntity<DiaryResponse> createDiary(
            @RequestBody DiaryCreate request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
         //mapper가 WebMvCTest에 적용이 안 돼서 직접 매핑으로 일단 변경
         //DiaryDto diaryDto = diaryMapper.toDtoFromRequest(request, userDetails.toDto());
        var diaryDto = request.toDtoFromCreate(userDetails.toDto());
        //저장
        //TODO : Image 의 아이디만을 request 로 받는 것은 좋지 않아 보인다. Client 작업 시에 어떻게 할 지 확인
        DiaryDto diary = diaryService.saveDiaryWithHashtagsAndImages(diaryDto, request.imageIds());

        return ResponseEntity.status(HttpStatus.CREATED).body(DiaryResponse.from(diary));
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
        // TODO : 로그인 안됐을 때 NullPointer 에러가 왜 나는 지 확인, SecurityConfig 확인 or 분기처리
        Long userId = userDetails.getId();
        var diaries = diaryService.getMyDiariesWithComments(userId,searchType, searchValue,pageable);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                diaries.map(DiaryWithCommentResponse::from)
        );
    }

    @GetMapping("/liked")
    @Operation(summary = "내가 좋아요 누른 다이어리 모두 조회")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DiaryWithCommentResponse.class))
    })
    public ResponseEntity<Page<DiaryWithCommentResponse>> getDiariesILiked(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ParameterObject @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC)  Pageable pageable
    ){
        Page<DiaryWithCommentDto> diaries = diaryService.getDiariesByUserLiked(userDetails.getId(), pageable);

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
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    })
    public ResponseEntity<DiaryWithCommentResponse> getDiaryWithComments(
            @PathVariable(name = "diaryId") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        DiaryWithCommentDto diary = diaryService.getDiaryWithComments(diaryId, userId);

        return ResponseEntity.ok(DiaryWithCommentResponse.from(diary));
    }

    @PreAuthorize("hasPermission(#diaryId, 'DIARY', 'UPDATE')")
    @PutMapping("/{diaryId}")
    @Operation(summary = "다이어리 수정 - 이미지 먼저 저장 or 삭제 후 진행")
    public ResponseEntity<Void> updateDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @RequestBody DiaryUpdate request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        //dto 로 변환
        var diaryDto = request.toDtoFromUpdate(diaryId, userDetails.toDto());
        diaryService.updateDiary(
                diaryDto,
                request.imageIds()
        );
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasPermission(#diaryId, 'DIARY', 'DELETE')")
    @DeleteMapping("/{diaryId}")
    @Operation(summary = "다이어리 삭제")
    public ResponseEntity<Void> deleteDiary(
            @PathVariable(name = "diaryId") Long diaryId
    ) {
        diaryService.deleteDiary(diaryId);
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
