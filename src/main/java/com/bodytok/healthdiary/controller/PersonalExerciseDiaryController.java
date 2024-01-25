package com.bodytok.healthdiary.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/diaries")
@RestController
public class PersonalExerciseDiaryController {

    @GetMapping
    public ResponseEntity<?> getAllDiaries() {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping
    public ResponseEntity<?> createDiary() {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<?> getDiary(@PathVariable Long diaryId) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<?> updateDiary(@PathVariable Long diaryId) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        return ResponseEntity.ok().build();
    }

}
