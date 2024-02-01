package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.PersonalExerciseDiaryDto;
import com.bodytok.healthdiary.dto.diary.DiaryRequest;
import com.bodytok.healthdiary.dto.diary.DiaryResponse;
import com.bodytok.healthdiary.service.PersonalExerciseDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
public class PersonalExerciseDiaryController {

    private final PersonalExerciseDiaryService diaryService;

    @GetMapping
    public ResponseEntity< Page<DiaryResponse>> getAllDiaries(
            @PageableDefault() Pageable pageable
    ) {
        Page<DiaryResponse> diaries = diaryService.getAllDiaries(pageable).map(DiaryResponse::from);

        return ResponseEntity.ok(diaries);
    }



    // 1. 작성 post 전 이미지 업로드
    // -> 이미지 먼저 생성
    // 2. 작성 post 할 때 이미지도 동시 업로드

    @PostMapping("/new")
    public ResponseEntity<DiaryResponse> createDiary(
            @RequestBody DiaryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        PersonalExerciseDiaryDto diary = diaryService.saveDiary(request.toDto(userDetails.toDto()));
        return ResponseEntity.ok(DiaryResponse.from(diary));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiary(@PathVariable Long diaryId) {
        PersonalExerciseDiaryDto diary = diaryService.getDiary(diaryId);
        return ResponseEntity.ok(DiaryResponse.from(diary));
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
