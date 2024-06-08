package com.bodytok.healthdiary.dto.exercise_routine.response;

import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;

import java.util.List;

public record RoutineWithExerciseResponse(
        Long id,
        String routineName,
        String memo,
        List<Exercise> exercises,
        Long userId
) {

    public static RoutineWithExerciseResponse of(Long id, String routineName, String memo, List<Exercise> exercises, Long userId) {
        return new RoutineWithExerciseResponse(id, routineName, memo, exercises, userId);
    }

    public static RoutineWithExerciseResponse from(RoutineDto dto) {
        return of(
                dto.id(),
                dto.routineName(),
                dto.memo(),
                dto.exercises(),
                dto.userAccountDto().id()
        );
    }
}
