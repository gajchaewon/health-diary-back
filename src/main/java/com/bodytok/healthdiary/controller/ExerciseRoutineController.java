package com.bodytok.healthdiary.controller;

import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineMapper;
import com.bodytok.healthdiary.dto.exercise_routine.request.ExerciseCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineUpdate;
import com.bodytok.healthdiary.dto.exercise_routine.response.RoutineWithExerciseResponse;
import com.bodytok.healthdiary.service.ExerciseRoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/routines")
@Tag(name = "ExerciseRoutine")
public class ExerciseRoutineController {
    private final RoutineMapper routineMapper = RoutineMapper.INSTANCE;
    private final ExerciseRoutineService exRoutineService;

    @GetMapping
    @Operation(summary = "유저 또는 나의 모든 루틴 조회")
    public ResponseEntity<List<RoutineWithExerciseResponse>> getAllRoutine(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(name = "userId",description = "루틴 조회 유저 ID", allowEmptyValue = true)
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
    @Operation(summary = "내 루틴 생성")
    public ResponseEntity<RoutineWithExerciseResponse> saveRoutine(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid RoutineCreate routineCreate
    ) {
        var toDto = routineMapper.toDtoFromCreate(routineCreate);
        RoutineDto dto = exRoutineService.saveRoutine(toDto, userDetails.getId());
        return ResponseEntity.ok().body(RoutineWithExerciseResponse.from(dto));
    }

    @PreAuthorize("hasPermission(#routineId, 'ROUTINE', 'UPDATE')")
    @PutMapping("/{routineId}")
    @Operation(summary = "내 루틴 업데이트")
    public ResponseEntity<RoutineWithExerciseResponse> updateRoutine(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "routineId") Long routineId,
            @RequestBody @Valid RoutineUpdate routineUpdate
    ){
        var toDto = routineMapper.toDtoFromUpdate(routineUpdate, routineId, userDetails.toDto());
        RoutineDto routineDto = exRoutineService.updateRoutine(toDto);
        return ResponseEntity.ok().body(RoutineWithExerciseResponse.from(routineDto));
    }

    @PreAuthorize("hasPermission(#routineId, 'ROUTINE', 'DELETE')")
    @DeleteMapping("/{routineId}")
    @Operation(summary = "내 루틴 삭제")
    public ResponseEntity<Void> deleteRoutine(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "routineId") Long routineId
    ){
        var toDto = routineMapper.toDtoFromDelete(routineId, userDetails.toDto());
        exRoutineService.deleteRoutine(toDto);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasPermission(#routineId, 'ROUTINE', 'UPDATE')")
    @PostMapping("/{routineId}/exercise")
    @Operation(summary = "내 운동 생성 (루틴 업데이트)")
    public ResponseEntity<RoutineWithExerciseResponse> saveExercise(
            @PathVariable(name = "routineId") Long routineId,
            @RequestBody @Valid ExerciseCreate exerciseCreate) {
        var toDto = exerciseCreate.toRoutineDto(routineId);
        RoutineDto routineDto = exRoutineService.saveExercise(toDto);
        return ResponseEntity.ok().body(RoutineWithExerciseResponse.from(routineDto));
    }

    @PreAuthorize("hasPermission(#routineId, 'ROUTINE', 'UPDATE')")
    @DeleteMapping("{routineId}/exercise/{exerciseId}")
    @Operation(summary = "내 운동 삭제 (루틴 업데이트)")
    public ResponseEntity<RoutineWithExerciseResponse> deleteExercise(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "routineId") Long routineId,
            @PathVariable(name = "exerciseId") String exerciseId
    ){
        var toDto = routineMapper.toDtoFromDelete(routineId, userDetails.toDto());
        RoutineDto routineDto = exRoutineService.deleteExercise(toDto, exerciseId);
        return ResponseEntity.ok().body(RoutineWithExerciseResponse.from(routineDto));
    }
}
