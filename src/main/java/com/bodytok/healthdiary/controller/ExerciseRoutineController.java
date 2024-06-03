package com.bodytok.healthdiary.controller;

import com.bodytok.healthdiary.domain.ExerciseRoutine;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.exercise_routine.ExerciseDto;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.dto.exercise_routine.request.ExerciseCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineCreate;
import com.bodytok.healthdiary.dto.exercise_routine.response.ExerciseResponse;
import com.bodytok.healthdiary.dto.exercise_routine.response.RoutineResponse;
import com.bodytok.healthdiary.service.ExerciseRoutineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/routines")
@Tag(name = "ExerciseRoutine")
public class ExerciseRoutineController {

    private final ExerciseRoutineService exRoutineService;

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
