package com.bodytok.healthdiary.dto.exercise_routine.response;

import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;

import java.util.List;
import java.util.stream.Collectors;

public record RoutineWithExerciseResponse(
        Long id,
        String routineName,
        String memo,
        List<ExerciseResponse> exerciseList,
        Long userId
) {

    public static RoutineWithExerciseResponse of(Long id, String routineName, String memo, List<ExerciseResponse> exerciseList, Long userId) {
        return new RoutineWithExerciseResponse(id, routineName, memo, exerciseList, userId);
    }

    public static RoutineWithExerciseResponse from(RoutineDto dto) {
        return of(
                dto.id(),
                dto.routineName(),
                dto.memo(),
                dto.exerciseDtoList().stream()
                        .map(ExerciseResponse::from)
                        .collect(Collectors.toList()),
                dto.userAccountDto().id()
        );
    }
}
