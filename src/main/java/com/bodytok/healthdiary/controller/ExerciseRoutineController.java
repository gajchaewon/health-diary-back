package com.bodytok.healthdiary.controller;

import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.exercise_routine.ExerciseDto;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.dto.exercise_routine.request.ExerciseCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineCreate;
import com.bodytok.healthdiary.dto.exercise_routine.response.ExerciseResponse;
import com.bodytok.healthdiary.dto.exercise_routine.response.RoutineWithExerciseResponse;
import com.bodytok.healthdiary.dto.exercise_routine.response.RoutineResponse;
import com.bodytok.healthdiary.service.ExerciseRoutineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/routines")
@Tag(name = "ExerciseRoutine")
public class ExerciseRoutineController {

    private final ExerciseRoutineService exRoutineService;

    @GetMapping
    public ResponseEntity<List<RoutineWithExerciseResponse>> getAllRoutine(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "userId", required = false) Long userId
    ){
        /**
         * TODO : 루틴 공개 해야하는 지 아닌 지 확인하고 비공개 or 공개 처리 로직 추가하기
         * 일단 공개로 만들기
         */
        Long requestUserId = userId == null ? userDetails.getId() : userId;
        List<RoutineDto> routineList = exRoutineService.getAllRoutine(requestUserId);
        var response = routineList.stream().map(RoutineWithExerciseResponse::from).toList();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<RoutineResponse> saveRoutine(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RoutineCreate routineCreate
    ) {
        RoutineDto dto = exRoutineService.saveRoutine(routineCreate, userDetails.getId());
        return ResponseEntity.ok().body(RoutineResponse.from(dto));
    }

    @PostMapping("/exercise")
    public ResponseEntity<ExerciseResponse> saveExercise(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ExerciseCreate exerciseCreate) {
        ExerciseDto dto = exRoutineService.saveExercise(exerciseCreate);
        return ResponseEntity.ok().body(ExerciseResponse.from(dto));
    }
}
