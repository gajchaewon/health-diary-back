package com.bodytok.healthdiary.dto.exercise_routine;

import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.dto.UserAccountDto;

import java.util.List;

public record RoutineDto(
        Long id,
        String routineName,
        String memo,
        UserAccountDto userAccountDto,
        List<Exercise> exercises
) {
        public static RoutineDto of(Long id, String routineName, String memo, UserAccountDto userAccountDto, List<Exercise> exercises) {
                return new RoutineDto(id, routineName, memo, userAccountDto, exercises);
        }

        public static RoutineDto from(Routine entity) {
                return RoutineDto.of(
                        entity.getId(),
                        entity.getRoutineName(),
                        entity.getMemo(),
                        UserAccountDto.from(entity.getUserAccount()),
                        entity.getExercises()
                );
        }

}
