package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.diary.PersonalExerciseDiaryWithCommentDto;
import com.bodytok.healthdiary.dto.diary.request.DiaryRequest;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
public class PersonalExerciseDiaryController {

    private final PersonalExerciseDiaryService diaryService;

    @GetMapping
    public ResponseEntity<Page<DiaryResponse>> getAllDiaries(
            @PageableDefault() Pageable pageable
    ) {
        Page<DiaryResponse> diaries = diaryService.getAllDiaries(pageable);

        return ResponseEntity.ok(diaries);
    }

    @PostMapping("/new")
    public ResponseEntity<DiaryResponse> createDiary(
            @RequestBody DiaryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        DiaryResponse diary = diaryService.saveDiaryWithHashtags(
                request.toDto(userDetails.toDto()),
                request.hashtags().stream()
                        .map(HashtagDto::of).collect(Collectors.toUnmodifiableSet()));
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryWithCommentResponse> getDiary(@PathVariable Long diaryId) {
        DiaryWithCommentResponse diary = DiaryWithCommentResponse.from(diaryService.getDiaryWithComments(diaryId));

        return ResponseEntity.ok(diary);
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<?> updateDiary(
            @PathVariable Long diaryId,
            @RequestBody DiaryRequest request
    ) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }

}
