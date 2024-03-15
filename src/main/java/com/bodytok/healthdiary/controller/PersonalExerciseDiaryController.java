package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diary.request.DiaryRequest;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<DiaryResponse>> getAllDiaries(
            @PageableDefault() Pageable pageable
    ) {
        Page<DiaryDto> diaries = diaryService.getAllDiaries(pageable);

        return ResponseEntity.ok().body(
                diaries.map(DiaryResponse::from)
        );
    }

    @PostMapping("/new")
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
    public ResponseEntity<DiaryWithCommentResponse> getDiaryWithComments(@PathVariable Long diaryId) {
        DiaryWithCommentDto diary = diaryService.getDiaryWithComments(diaryId);

        return ResponseEntity.ok(DiaryWithCommentResponse.from(diary));
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @PathVariable Long diaryId,
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
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }

}
