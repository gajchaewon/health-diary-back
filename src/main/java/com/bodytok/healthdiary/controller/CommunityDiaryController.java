package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.constant.SearchType;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.service.CommunityExerciseDiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community")
public class CommunityDiaryController {

    private final CommunityExerciseDiaryService communityDiaryService;

    @GetMapping
    @Operation(summary = "모든 커뮤니티 다이어리 가져오기(댓글 미포함) / title, content, nickname, 해시태그 검색 가능")
    @SecurityRequirements(value = {}) // Swagger 글로벌 security 설정 지우기
    public ResponseEntity<Page<DiaryResponse>> getAllDiaries(
            @Parameter(name = "searchType",description = "검색하고자 하는 필드")
            @RequestParam(required = false,name = "searchType") SearchType searchType,
            @Parameter(name = "searchValue",description = "검색 키워드 - 검색 타입에 맞게 검색")
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @ParameterObject @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<DiaryDto> diaries = communityDiaryService.getAllDiaries(searchType, searchValue, pageable);

        return ResponseEntity.ok().body(
                diaries.map(DiaryResponse::from)
        );
    }

    @GetMapping("/{userId}")
    @Operation(summary = "유저의 모든 커뮤니티 다이어리 가져오기 - 댓글 미포함")
    public ResponseEntity<Page<DiaryWithCommentResponse>> getUserDiariesWithComments(
            @PathVariable("userId") Long userId,
            @ParameterObject @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ){
        var diaries = communityDiaryService.getUserDiariesWithCommentsByUserId(userId, pageable);
        return ResponseEntity.ok().body(
                diaries.map(DiaryWithCommentResponse::from)
        );
    }

}
