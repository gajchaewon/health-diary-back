package com.bodytok.healthdiary.dto.exercise_routine;

import com.bodytok.healthdiary.domain.ExerciseRoutine;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.dto.UserAccountDto;

import java.util.List;

public record RoutineDto(
        Long id,
        String routineName,
        String memo,
        UserAccountDto userAccountDto,
        List<ExerciseRoutine> exerciseRoutines
) {
        public static RoutineDto of(Long id, String routineName, String memo, UserAccountDto userAccountDto, List<ExerciseRoutine> exerciseRoutines) {
                return new RoutineDto(id, routineName, memo, userAccountDto, exerciseRoutines);
        }

        public static RoutineDto from(Routine entity) {
                return RoutineDto.of(
                        entity.getId(),
                        entity.getRoutineName(),
                        entity.getMemo(),
                        UserAccountDto.from(entity.getUserAccount()),
                        entity.getExerciseRoutines()
                );
        }

}
