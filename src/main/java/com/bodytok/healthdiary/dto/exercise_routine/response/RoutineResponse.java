package com.bodytok.healthdiary.dto.exercise_routine.response;

import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;

public record RoutineResponse(
        Long id,
        String routineName,
        String memo,
        Long userId
) {

    public static RoutineResponse of(Long id, String routineName, String memo, Long userId) {
        return new RoutineResponse(id, routineName, memo, userId);
    }

    public static RoutineResponse from(RoutineDto dto) {
        return of(
                dto.id(),
                dto.routineName(),
                dto.memo(),
                dto.userAccountDto().id()
        );
    }
}
